package com.newfiber.workflow.service.impl;

import com.newfiber.core.exception.BizException;
import com.newfiber.workflow.entity.WorkflowModel;
import com.newfiber.workflow.entity.request.WorkflowModelPageRequest;
import com.newfiber.workflow.entity.response.WorkflowModelNextTaskResponse;
import com.newfiber.workflow.service.ActivitiModelService;
import com.newfiber.workflow.utils.PageWrapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.Task;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ActivitiModelServiceImpl implements ActivitiModelService {

    @Resource
    private RepositoryService repositoryService;

    @Override
    public void delete(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Override
    public void upload(MultipartFile multipartFile) {
        try {
            String filename = multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            repositoryService.createDeployment().addInputStream(filename, is).deploy();
        } catch (Exception e) {
            log.error("【工作流模型】上传工作流模型文件失败：{}--{}", e.getMessage(), e.getStackTrace());
        }
    }

    @Override
    public List<WorkflowModelNextTaskResponse> nextTasks(String workflowKey, String currentTask) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(workflowKey).singleResult();
        if(null == processDefinition){
            throw new BizException(String.format("工作流【%s】不存在", workflowKey));
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        List<FlowElement> flowElementList = (List<FlowElement>) bpmnModel.getMainProcess().getFlowElements();

        List<FlowElement> currentActivityFlowElementList = flowElementList.stream().filter(t -> t instanceof Task).filter(
                t -> t.getId().equals(currentTask)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(currentActivityFlowElementList)){
            throw new BizException(String.format("任务节点【%s】不存在", currentTask));
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
                WorkflowModelNextTaskResponse.append(responses, (Task) flowElement);
            }else if(flowElement instanceof ExclusiveGateway){
                List<FlowElement> nextTasks = flowElementList.stream().filter(
                        t -> t instanceof SequenceFlow).filter(t -> ((SequenceFlow) t).getSourceRef().equals(flowElement.getId())).collect(Collectors.toList());
                WorkflowModelNextTaskResponse.append(responses, nextTasks);
            }
        }

        return responses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageWrapper<WorkflowModel> pageWorkflowModel(WorkflowModelPageRequest request) {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

        PageWrapper processDefinitionPageWrapper = new PageWrapper<>(processDefinitionList, request);

        List<WorkflowModel> workflowModelList = new ArrayList<>();
        for(Object object : processDefinitionPageWrapper.getPage(request.getPageNum())){
            ProcessDefinition processDefinition = (ProcessDefinition) object;
            WorkflowModel workflowModel = new WorkflowModel();
            workflowModel.setId(processDefinition.getId());
            workflowModel.setDeploymentId(processDefinition.getDeploymentId());
            workflowModel.setVersion(processDefinition.getVersion());
            workflowModel.setCategory(processDefinition.getCategory());

            workflowModel.setKey(processDefinition.getKey());
            workflowModel.setName(processDefinition.getName());
            workflowModel.setResourceName(processDefinition.getResourceName());
            workflowModel.setDiagramResourceName(processDefinition.getDiagramResourceName());
            workflowModel.setDescription(processDefinition.getDescription());
            workflowModelList.add(workflowModel);
        }

        processDefinitionPageWrapper.setList(workflowModelList);
        return processDefinitionPageWrapper;
    }

}
