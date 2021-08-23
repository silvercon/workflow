package com.newfiber.workflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.newfiber.workflow.entity.WorkflowModel;
import com.newfiber.workflow.entity.request.WorkflowModeCreateRequest;
import com.newfiber.workflow.entity.request.WorkflowModelPageRequest;
import com.newfiber.workflow.entity.response.WorkflowModelNextTaskResponse;
import com.newfiber.workflow.entity.support.WorkflowModelMetaInfo;
import com.newfiber.workflow.service.ActivitiModelService;
import com.newfiber.workflow.utils.PageWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.Task;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ActivitiModelServiceImpl implements ActivitiModelService {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Model create(WorkflowModeCreateRequest request) {
        Model model = repositoryService.newModel();
        model.setKey(request.getKey());
        model.setName(request.getName());
        model.setMetaInfo(JSONObject.toJSONString(new WorkflowModelMetaInfo(request.getName(), request.getDescription())));
        repositoryService.saveModel(model);
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String modelId, String key, String name, String jsonXml, String svgXml, String description) {
        Model model = repositoryService.getModel(modelId);
        if(null == model){
            throw new ActivitiException(String.format("模型编号【%s】不存在", modelId));
        }

        try{
            model.setKey(key);
            model.setName(name);
            model.setMetaInfo(JSONObject.toJSONString(new WorkflowModelMetaInfo(name, description)));

            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            log.error("保存工作流失败：{}-->{}", e.getMessage(), e.getStackTrace());
            throw new ActivitiException(e.getMessage());
        }

    }

    @Override
    public void deploy(String modelId) {
        Model model = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(model.getId());
        if (bytes == null) {
            throw new ActivitiException("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }

        try {
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(new ObjectMapper().readTree(bytes));
            if (bpmnModel.getProcesses().size() == 0) {
                throw new ActivitiException("数据模型不符要求，请至少设计一条主线流程。");
            }

            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            //发布流程
            String processSourceName = model.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(model.getName())
                    .addString(processSourceName, new String(bpmnBytes, StandardCharsets.UTF_8)).deploy();

            // 发布版本+1
            Integer version = model.getVersion();
            model.setVersion(StrUtil.isBlank(model.getDeploymentId()) ? version : version + 1);
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);

        } catch (IOException e) {
            log.error("流程发布失败：{}-->{}", e.getMessage(), e.getStackTrace());
            throw new ActivitiException(e.getMessage());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String modelId) {
        Model model = repositoryService.getModel(modelId);
        if(null == model){
            throw new ActivitiException(String.format("模型编号【%s】不存在", modelId));
        }

        if(null != model.getDeploymentId()){
            repositoryService.deleteDeployment(model.getDeploymentId());
        }

        repositoryService.deleteModel(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(String workflowKey, MultipartFile multipartFile) {
        try {

            String filename = multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            Deployment deployment = repositoryService.createDeployment().addInputStream(filename, is).key(workflowKey).deploy();

            // 更新模型
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(workflowKey).latestVersion().singleResult();
            if(null == processDefinition){
                throw new ActivitiException("模型文件上传失败，请检查文件是否正确");
            }

            Model model = repositoryService.newModel();
            model.setKey(processDefinition.getKey());
            model.setName(processDefinition.getName());
            model.setDeploymentId(deployment.getId());
            model.setCategory(processDefinition.getCategory());
            model.setVersion((int) repositoryService.createModelQuery().modelKey(workflowKey).count() + 1);
            model.setMetaInfo(JSONObject.toJSONString(new WorkflowModelMetaInfo(processDefinition.getName(), null)));
            repositoryService.saveModel(model);

            // 保存流程json信息
            ObjectNode objectNode = parseBpmn2Json(multipartFile);
            repositoryService.addModelEditorSource(model.getId(), objectNode.toString().getBytes());

        } catch (Exception e) {
            log.error("【工作流模型】上传工作流模型文件失败：{}--{}", e.getMessage(), e.getStackTrace());
            throw new ActivitiException(e.getMessage());
        }
    }

    private ObjectNode parseBpmn2Json(MultipartFile multipartFile) throws IOException, XMLStreamException {
        InputStream bpmnStream = new ByteArrayInputStream(multipartFile.getBytes());
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        InputStreamReader inputStreamReader = new InputStreamReader(bpmnStream, StandardCharsets.UTF_8);
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStreamReader);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlStreamReader);
        return (new BpmnJsonConverter()).convertToJson(bpmnModel);
    }

    @Override
    public List<WorkflowModelNextTaskResponse> nextTasks(String workflowKey, String currentTask) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(workflowKey).singleResult();
        if(null == processDefinition){
            throw new ActivitiException(String.format("工作流【%s】不存在", workflowKey));
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        List<FlowElement> flowElementList = (List<FlowElement>) bpmnModel.getMainProcess().getFlowElements();

        List<FlowElement> currentActivityFlowElementList = flowElementList.stream().filter(t -> t instanceof Task).filter(
                t -> t.getId().equals(currentTask)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(currentActivityFlowElementList)){
            throw new ActivitiException(String.format("任务节点【%s】不存在", currentTask));
        }

        Task currentTaskFlowElement = (Task) currentActivityFlowElementList.get(0);
        List<String> currentTaskTargetFlowElementIdList = currentTaskFlowElement.getOutgoingFlows().stream().
                map(SequenceFlow::getTargetRef).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(currentTaskTargetFlowElementIdList)){
            return Collections.emptyList();
        }

        List<FlowElement> currentTaskTargetFlowElement = flowElementList.stream().filter(t ->
                currentTaskTargetFlowElementIdList.contains(t.getId())).collect(Collectors.toList());

        List<WorkflowModelNextTaskResponse> responses = new ArrayList<>();
        for(FlowElement flowElement : currentTaskTargetFlowElement){
            if(flowElement instanceof Task){
                String conditionExpression = "";
                List<SequenceFlow> sequenceFlowList = ((Task) flowElement).getIncomingFlows().stream().filter(
                        t -> t.getSourceRef().equals(currentTask) && t.getTargetRef().equals(flowElement.getId())).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(sequenceFlowList)){
                    conditionExpression = sequenceFlowList.get(0).getConditionExpression();
                }
                WorkflowModelNextTaskResponse.append(responses, (Task) flowElement, conditionExpression);
            }else if(flowElement instanceof ExclusiveGateway){
                List<FlowElement> nextTasks = flowElementList.stream().filter(
                        t -> t instanceof SequenceFlow).filter(t -> ((SequenceFlow) t).getSourceRef().equals(flowElement.getId())).collect(Collectors.toList());
                WorkflowModelNextTaskResponse.append(responses, nextTasks);
            }
        }

        return responses;
    }

    @Override
    public WorkflowModel detail(String modelId) {
        ModelEntity modelEntity = (ModelEntity) repositoryService.getModel(modelId);
        if(null == modelEntity){
            throw new ActivitiException(String.format("模型编号【%s】不存在", modelId));
        }

        WorkflowModel workflowModel = new WorkflowModel();
        BeanUtils.copyProperties(modelEntity, workflowModel);

        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        if (null != modelEditorSource) {
            try {
                workflowModel.setEditorSource((ObjectNode) objectMapper.readTree(new String(modelEditorSource, StandardCharsets.UTF_8)));
            } catch (JsonProcessingException e) {
                log.error("查询模型失败：{}-->{}", e.getMessage(), e.getStackTrace());
                throw new ActivitiException(String.format("查询模型失败【%s】", e.getMessage()));
            }
        }

        byte[] modelEditorSourceExt = repositoryService.getModelEditorSourceExtra(modelId);
        if(null != modelEditorSourceExt){
            workflowModel.setEditorSourceExt(new String(modelEditorSourceExt, StandardCharsets.UTF_8));
        }

        WorkflowModelMetaInfo workflowModelMetaInfo = JSONObject.parseObject(modelEntity.getMetaInfo(), WorkflowModelMetaInfo.class);
        workflowModel.setDescription(workflowModelMetaInfo.getDescription());

        return workflowModel;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PageWrapper<WorkflowModel> pageWorkflowModel(WorkflowModelPageRequest request) {
        List<Model> modelList = repositoryService.createModelQuery().orderByModelId().desc().list();

        if(StringUtils.isNotBlank(request.getName())){
            modelList = modelList.stream().filter(t -> StringUtils.isNotBlank(t.getName()) &&
                    t.getName().contains(request.getName())).collect(Collectors.toList());
        }

        PageWrapper<WorkflowModel> workflowModelPageWrapper = new PageWrapper(modelList, request);

        List<WorkflowModel> workflowModelList = new ArrayList<>();
        for(Object object : workflowModelPageWrapper.getPage(request.getPageNum())){
            Model model = (Model) object;
            WorkflowModel workflowModel = new WorkflowModel();
            BeanUtils.copyProperties(model, workflowModel);
            if(null != model.getDeploymentId()){
                workflowModel.setDeploymentFlag(true);
            }
            workflowModelList.add(workflowModel);
        }

        workflowModelPageWrapper.setList(workflowModelList);
        return workflowModelPageWrapper;
    }

}
