package com.newfiber.workflow.service.impl;

import com.newfiber.core.base.WorkflowPageReq;
import com.newfiber.core.base.WorkflowSubmitReq;
import com.newfiber.core.exception.BizException;
import com.newfiber.core.result.PageInfo;
import com.newfiber.workflow.entity.WorkflowHistoricActivity;
import com.newfiber.workflow.enums.EConstantValue;
import com.newfiber.workflow.enums.IWorkflowActivityType.EventActivity;
import com.newfiber.workflow.enums.IWorkflowActivityType.TaskActivity;
import com.newfiber.workflow.service.ActivitiProcessService;
import com.newfiber.workflow.support.IWorkflowCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivitiProcessServiceImpl implements ActivitiProcessService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Override
    public String startWorkflow(Object businessKey, IWorkflowCallback<?> callback) {
        return startWorkflow(businessKey, callback, new HashMap<>(1));
    }

    @Override
    public String startWorkflow(Object businessKey, IWorkflowCallback<?> callback, Map<String, Object> variables) {
        variables.putIfAbsent(EConstantValue.IWorkflowCallback.getValue(), callback.getClass().getName());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(callback.getWorkflowDefinition().getWorkflowKey(),
                businessKey.toString(), variables);

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        if(null != task){
            callback.refreshStatus(businessKey, task.getTaskDefinitionKey());
            callback.refreshWorkflowInstanceId(businessKey, processInstance.getProcessInstanceId());
        }

        return processInstance.getProcessInstanceId();
    }

    @Override
    public String submitWorkflow(Object businessKey, Object submitUser, WorkflowSubmitReq submitReq, IWorkflowCallback<?> callback) {
        return submitWorkflow(businessKey, submitUser, submitReq.getApproveResult(), callback);
    }

    @Override
    public String submitWorkflow(Object businessKey, Object submitUser, String approveResult, IWorkflowCallback<?> callback) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(
                businessKey.toString(), callback.getWorkflowDefinition().getWorkflowKey()).singleResult();
        if(null == processInstance){
            throw new BizException(String.format("业务编号【%s】在工作流【%s】中不存在可执行实例",
                    businessKey.toString(), callback.getWorkflowDefinition().getWorkflowName()));
        }

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        if(null == task){
            throw new BizException(String.format("业务编号【%s】在工作流【%s】中不存在可执行任务",
                    businessKey.toString(), callback.getWorkflowDefinition().getWorkflowName()));
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put(EConstantValue.ApproveResultField.getValue(), approveResult);

        taskService.claim(task.getId(), submitUser.toString());
        taskService.complete(task.getId(), variables);

        task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        if(null != task){
            callback.refreshStatus(businessKey, task.getTaskDefinitionKey());
        }

        return null;
    }

    @Override
    public List<String> listTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, null, workflowKey, taskKey);
        List<Task> taskList = taskQuery.list();
        return listTaskBusinessKey(taskList);
    }

    @Override
    public List<String> listTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, groupId, workflowKey, taskKey);
        List<Task> taskList = taskQuery.list();
        return listTaskBusinessKey(taskList);
    }

    @Override
    public List<String> listTaskDoneBusinessKeyByUser(String workflowKey, String taskKey, Object userId) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(workflowKey).finished();
        if(null != userId && StringUtils.isNotBlank(userId.toString())){
            historicTaskInstanceQuery.taskInvolvedUser(userId.toString());
        }
        if(StringUtils.isNotBlank(taskKey)){
            historicTaskInstanceQuery.taskId(taskKey);
        }

        List<HistoricTaskInstance> taskList = historicTaskInstanceQuery.list();

        return listHistoryTaskBusinessKey(taskList);
    }

    @Override
    public List<String> listInvolvedBusinessKeyByUser(String workflowKey, String taskKey, Object userId) {
        return null;
    }

    @Override
    public PageInfo<String> pageTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId, WorkflowPageReq workflowPageReq) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, groupId, workflowKey, taskKey);
        List<Task> taskList = taskQuery.listPage(workflowPageReq.pageStart(), workflowPageReq.getPageSize());
        List<String> businessKeyList = listTaskBusinessKey(taskList);
        // TODO

        return null;
    }

    @Override
    public PageInfo<String> pageTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId, WorkflowPageReq workflowPageReq) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, null, workflowKey, taskKey);
        List<Task> taskList = taskQuery.listPage(workflowPageReq.pageStart(), workflowPageReq.getPageSize());
        List<String> businessKeyList = listTaskBusinessKey(taskList);

        return null;
    }

    @Override
    public List<WorkflowHistoricActivity> listHistoricActivity(String workflowKey, Object businessKey, String workflowUserId, String status) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(workflowKey)
                .processInstanceBusinessKey(businessKey.toString()).singleResult();
        if(null == historicProcessInstance){
            throw new BizException(String.format("业务编号【%s】不存在工作流【%s】", businessKey.toString(), workflowKey));
        }

        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(historicProcessInstance.getId()).orderByHistoricActivityInstanceStartTime().asc();
        if(StringUtils.isNotBlank(workflowUserId)){
            historicActivityInstanceQuery.taskAssignee(workflowUserId);
        }
        if(StringUtils.isNotBlank(status)){
            historicActivityInstanceQuery.activityId(status);
        }

        List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceQuery.list();

        historicActivityInstanceList = historicActivityInstanceList.stream().filter(t -> t.getActivityType().contains(
                EventActivity.Event.getTypeKey()) || t.getActivityType().contains(TaskActivity.Task.getTypeKey())).collect(Collectors.toList());

        List<WorkflowHistoricActivity> workflowHistoricActivityList = new ArrayList<>();
        for(HistoricActivityInstance historicActivityInstance : historicActivityInstanceList){
            workflowHistoricActivityList.add(WorkflowHistoricActivity.build(historicActivityInstance));
        }

        return workflowHistoricActivityList;
    }

    private ArrayList<String> listTaskBusinessKey(List<Task> taskList) {
        Set<String> businessKeySet = new HashSet<>();
        Set<String> processInstanceIdSet = taskList.stream().map(TaskInfo::getProcessInstanceId).collect(Collectors.toSet());
        for(String processInstanceId : processInstanceIdSet){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKeySet.add(processInstance.getBusinessKey());
        }
        return new ArrayList<>(businessKeySet);
    }

    private ArrayList<String> listHistoryTaskBusinessKey(List<HistoricTaskInstance> taskList) {
        Set<String> businessKeySet = new HashSet<>();
        Set<String> processInstanceIdSet = taskList.stream().map(TaskInfo::getProcessInstanceId).collect(Collectors.toSet());
        for(String processInstanceId : processInstanceIdSet){
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKeySet.add(processInstance.getBusinessKey());
        }
        return new ArrayList<>(businessKeySet);
    }

    private TaskQuery wrapperTaskQuery(Object userId, Object groupId, String workflowKey,
            String taskKey) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if(null != userId && StringUtils.isNotBlank(userId.toString())){
            taskQuery.taskCandidateUser(userId.toString());
        }
        if(null != groupId && StringUtils.isNotBlank(groupId.toString())){
            taskQuery.taskCandidateGroup(groupId.toString());
        }
        if(StringUtils.isNotBlank(workflowKey)){
            taskQuery.processDefinitionKey(workflowKey);
        }
        if(StringUtils.isNotBlank(taskKey)){
            taskQuery.taskDefinitionKey(taskKey);
        }
        return taskQuery;
    }

}
