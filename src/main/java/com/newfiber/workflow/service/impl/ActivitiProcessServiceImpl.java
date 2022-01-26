package com.newfiber.workflow.service.impl;

import com.newfiber.core.result.PageInfo;
import com.newfiber.workflow.entity.WorkflowHistoricActivity;
import com.newfiber.workflow.entity.WorkflowUser;
import com.newfiber.workflow.enums.EBoolean;
import com.newfiber.workflow.enums.EConstantValue;
import com.newfiber.workflow.enums.IWorkflowActivityType.EventActivity;
import com.newfiber.workflow.enums.IWorkflowActivityType.TaskActivity;
import com.newfiber.workflow.service.ActivitiProcessService;
import com.newfiber.workflow.support.IWorkflowCallback;
import com.newfiber.workflow.support.request.WorkflowPageReq;
import com.newfiber.workflow.support.request.WorkflowStartReq;
import com.newfiber.workflow.support.request.WorkflowSubmitReq;
import com.newfiber.workflow.utils.NotificationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class ActivitiProcessServiceImpl implements ActivitiProcessService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private IdentityService identityService;

    @Resource
    private NotificationService notificationService;

    @Override
    public String startWorkflow(IWorkflowCallback<?> callback, Object businessKey) {
        return startWorkflow(callback, businessKey, new HashMap<>());
    }

    @Override
    public String startWorkflow(IWorkflowCallback<?> workflowCallback, Object businessKey, WorkflowStartReq startReq) {
        Map<String, Object> variables = new HashMap<>();
        if(null != startReq && StringUtils.isNotBlank(startReq.getNextTaskApproveUserId())){
            variables.put(EConstantValue.ApproveUserIdField.getValue(), startReq.getNextTaskApproveUserId());
        }
        if(null != startReq && StringUtils.isNotBlank(startReq.getNextTaskApproveRoleId())){
            variables.put(EConstantValue.ApproveRoleIdField.getValue(), startReq.getNextTaskApproveRoleId());
        }
        if(null != startReq && !CollectionUtils.isEmpty(startReq.getNextTaskApproveUserIdList())){
            variables.put(EConstantValue.ApproveUserIdListField.getValue(), startReq.getNextTaskApproveUserIdList());
        }
        if(null != startReq && !CollectionUtils.isEmpty(startReq.getNotificationTemplateArgs())){
            variables.put(EConstantValue.NotificationTemplateArgs.getValue(), startReq.getNotificationTemplateArgs());
        }
        return startWorkflow(workflowCallback, businessKey, variables);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startWorkflow(IWorkflowCallback<?> callback, Object businessKey, Map<String, Object> variables) {
        variables.putIfAbsent(EConstantValue.IWorkflowCallback.getValue(), callback.getClass().getName());
        variables.putIfAbsent(EConstantValue.ApproveResultField.getValue(), EBoolean.True.getStringValue());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(callback.getWorkflowDefinition().getWorkflowKey(),
                businessKey.toString(), variables);

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();

        if(!CollectionUtils.isEmpty(taskList)){
            callback.refreshStatus(businessKey, taskList.get(0).getTaskDefinitionKey());
            callback.refreshWorkflowInstanceId(businessKey, processInstance.getProcessInstanceId());
        }

        return processInstance.getProcessInstanceId();
    }

    @Override
    public String submitWorkflow(IWorkflowCallback<?> callback, Object businessKey, WorkflowSubmitReq submitReq) {
        return submitWorkflow(callback, businessKey, submitReq.getSubmitUserId(), submitReq.getApproveResult(),
	        submitReq.getApproveComment(), submitReq.getNextTaskApproveUserId(),
	        submitReq.getNextTaskApproveUserIdList(), submitReq.getNextTaskApproveRoleId(), submitReq.getNotificationTemplateArgs());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitWorkflow(IWorkflowCallback<?> callback, Object businessKey, Object submitUser, String approveResult,
	        String approveComment, String nextTaskApproveUserId, List<String> nextTaskApproveUserIdList,
            String nextTaskApproveRoleId, List<String> notificationTemplateArgs) {
        User user = identityService.createUserQuery().userId(submitUser.toString()).singleResult();
        if(null == user){
            throw new ActivitiException(String.format("用户【%s】不存在", submitUser.toString()));
        }

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(
                businessKey.toString(), callback.getWorkflowDefinition().getWorkflowKey()).singleResult();
        if(null == processInstance){
            throw new ActivitiException(String.format("业务编号【%s】在工作流【%s】中不存在可执行实例",
                    businessKey.toString(), callback.getWorkflowDefinition().getWorkflowName()));
        }

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).
                taskCandidateOrAssigned(submitUser.toString()).singleResult();

        if(null == task){
            throw new ActivitiException(String.format("用户【%s】在工作流【%s】的业务编号【%s】中不存在可执行任务",
                    user.getFirstName(), callback.getWorkflowDefinition().getWorkflowName(), businessKey.toString()));
        }

//        Set<String> taskUserList = listTaskUser(task);
//        if(null != taskUserList && !taskUserList.contains(submitUser.toString())){
//            throw new ActivitiException(String.format("提交失败，用户【%s(%s)】不存在审核权限", user.getFirstName(), user.getId()));
//        }

        Map<String, Object> variables = new HashMap<>(1);
        variables.put(EConstantValue.ApproveResultField.getValue(), approveResult);

        // 任务本地变量
        Map<String, Object> transientVariables = new HashMap<>(2);
        if(StringUtils.isNotBlank(nextTaskApproveUserId)){
            transientVariables.put(EConstantValue.ApproveUserIdField.getValue(), nextTaskApproveUserId);
        }
        if(!CollectionUtils.isEmpty(nextTaskApproveUserIdList)){
            transientVariables.put(EConstantValue.ApproveUserIdListField.getValue(), nextTaskApproveUserIdList);
        }
        if(StringUtils.isNotBlank(nextTaskApproveRoleId)){
            transientVariables.put(EConstantValue.ApproveRoleIdField.getValue(), nextTaskApproveRoleId);
        }
        if(!CollectionUtils.isEmpty(notificationTemplateArgs)){
            transientVariables.put(EConstantValue.NotificationTemplateArgs.getValue(), notificationTemplateArgs);
        }

		// 认领并完成任务
		if (StringUtils.isNotBlank(approveComment)) {
			taskService.addComment(task.getId(), task.getProcessInstanceId(), approveComment);
		}
		taskService.claim(task.getId(), submitUser.toString());
		taskService.complete(task.getId(), variables, transientVariables);

        // 更新状态
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        if(!CollectionUtils.isEmpty(taskList)){
            Set<String> taskDefinitionKeySet = taskList.stream().map(TaskInfo::getTaskDefinitionKey).collect(Collectors.toSet());
            if(taskDefinitionKeySet.size() == 1){
                callback.refreshStatus(businessKey, taskDefinitionKeySet.iterator().next());
            }
        }

        return businessKey.toString();
    }

    @Override
    public boolean sendEmailNotification(String email, String content) {
        boolean result;
        if(StringUtils.isBlank(email) || StringUtils.isBlank(content)){
            result = false;
        }else{
            result = notificationService.sendSimpleMail(email, EConstantValue.EmailNotificationSubject.getValue(), content);
        }
        log.info("邮件发送{} | 邮箱：{} | 内容：{}", result ? "成功" : "失败" , email, content);
        return result;
    }

    @Override
    public boolean sendSmsNotification(String mobile, String smsSign, String smsTemplateCode, List<String> templateArgs) {
        boolean result;
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(smsSign) || StringUtils.isBlank(smsTemplateCode)){
            result = false;
        }else{
            result = notificationService.sendTencentSms(mobile, smsSign, smsTemplateCode, templateArgs);
        }
        log.info("短信发送{} | 手机号：{} | 模板编号：{}", result ? "成功" : "失败" , mobile, smsTemplateCode);
        return result;
    }

    @Override
    public List<String> listTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, null, workflowKey, taskKey);
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
        List<String> todoBusinessKey = listTodoBusinessKeyByUser(workflowKey, taskKey, userId);
        List<String> taskDoneBusinessKey = listTaskDoneBusinessKeyByUser(workflowKey, taskKey, userId);
        todoBusinessKey.addAll(taskDoneBusinessKey);
        return new ArrayList<>(new HashSet<>(todoBusinessKey));
    }

    @Override
    public List<String> listTodoBusinessKeyByUser(IWorkflowCallback<?> callback, String taskKey, Object userId) {
        return listTodoBusinessKeyByUser(callback.getWorkflowDefinition().getWorkflowKey(), taskKey, userId);
    }

    @Override
    public List<String> listTaskDoneBusinessKeyByUser(IWorkflowCallback<?> callback, String taskKey, Object userId) {
        return listTaskDoneBusinessKeyByUser(callback.getWorkflowDefinition().getWorkflowKey(), taskKey, userId);
    }

    @Override
    public List<String> listInvolvedBusinessKeyByUser(IWorkflowCallback<?> callback, String taskKey, Object userId) {
        return listInvolvedBusinessKeyByUser(callback.getWorkflowDefinition().getWorkflowKey(), taskKey, userId);
    }

    @Override
    public List<String> listTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, groupId, workflowKey, taskKey);
        List<Task> taskList = taskQuery.list();
        return listTaskBusinessKey(taskList);
    }

    @Override
    public List<WorkflowUser> listTodoBusinessExecutor(String workflowKey, Object businessKey) {

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(
                businessKey.toString(), workflowKey).singleResult();
        if(null == processInstance){
            return Collections.emptyList();
        }

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        if(null == task){
            return Collections.emptyList();
        }

        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
        if(!CollectionUtils.isEmpty(identityLinkList)){
            List<User> userList = new ArrayList<>();
            for(IdentityLink identityLink : identityLinkList){
                if(StringUtils.isNotBlank(identityLink.getUserId())){
                    userList.add(identityService.createUserQuery().userId(identityLink.getUserId()).singleResult());
                }
                if(StringUtils.isNotBlank(identityLink.getGroupId())){
                    List<User> groupUserList = identityService.createUserQuery().memberOfGroup(identityLink.getGroupId()).list();
                    userList.addAll(groupUserList);
                }
            }
            return WorkflowUser.build(userList);
        }

        return Collections.emptyList();
    }

    @Override
    public PageInfo<String> pageTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId, WorkflowPageReq workflowPageReq) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, groupId, workflowKey, taskKey);
        List<Task> taskList = taskQuery.listPage(workflowPageReq.pageStart(), workflowPageReq.getPageSize());
        List<String> businessKeyList = listTaskBusinessKey(taskList);
        return new PageInfo<>(workflowPageReq, businessKeyList, taskQuery.count());
    }

    @Override
    public PageInfo<String> pageDoneBusinessKeyByUser(String workflowKey, String taskKey, Object userId, WorkflowPageReq workflowPageReq) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(workflowKey).finished();
        if(null != userId && StringUtils.isNotBlank(userId.toString())){
            historicTaskInstanceQuery.taskInvolvedUser(userId.toString());
        }
        if(StringUtils.isNotBlank(taskKey)){
            historicTaskInstanceQuery.taskId(taskKey);
        }
        historicTaskInstanceQuery.listPage(workflowPageReq.pageStart(), workflowPageReq.getPageSize());

        List<HistoricTaskInstance> taskList = historicTaskInstanceQuery.list();
        List<String> strings = listHistoryTaskBusinessKey(taskList);
        return new PageInfo<>(workflowPageReq, strings, historicTaskInstanceQuery.count());
    }

    @Override
    public PageInfo<String> pageTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId, WorkflowPageReq workflowPageReq) {
        TaskQuery taskQuery = wrapperTaskQuery(userId, null, workflowKey, taskKey);
        List<Task> taskList = taskQuery.listPage(workflowPageReq.pageStart(), workflowPageReq.getPageSize());
        List<String> businessKeyList = listTaskBusinessKey(taskList);
        return new PageInfo<>(workflowPageReq, businessKeyList, taskQuery.count());
    }

    @Override
    public List<WorkflowHistoricActivity> listHistoricActivity(String workflowKey, Object businessKey, String workflowUserId, String status) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(workflowKey)
                .processInstanceBusinessKey(businessKey.toString()).singleResult();
        if(null == historicProcessInstance){
            throw new ActivitiException(String.format("业务编号【%s】不存在工作流【%s】", businessKey.toString(), workflowKey));
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

    private TaskQuery wrapperTaskQuery(Object userId, Object groupId, String workflowKey, String taskKey) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if(null != userId && StringUtils.isNotBlank(userId.toString())){
            taskQuery.taskCandidateOrAssigned(userId.toString());
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
