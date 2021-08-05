package com.newfiber.workflow.service;

import com.newfiber.core.base.WorkflowPageReq;
import com.newfiber.core.base.WorkflowSubmitReq;
import com.newfiber.core.result.PageInfo;
import com.newfiber.workflow.entity.WorkflowHistoricActivity;
import com.newfiber.workflow.support.IWorkflowCallback;
import java.util.List;
import java.util.Map;

public interface ActivitiProcessService {

    String startWorkflow(Object businessKey, IWorkflowCallback callback);

    String startWorkflow(Object businessKey, IWorkflowCallback callback, Map<String, Object> variables);

    String submitWorkflow(Object businessKey, Object submitUser, WorkflowSubmitReq submitReq, IWorkflowCallback callback);

    PageInfo<String> pageTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId, WorkflowPageReq workflowPageReq);

    List<String> listTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId);

    PageInfo<String> pageTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId, WorkflowPageReq workflowPageReq);

    List<String> listTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId);

    List<WorkflowHistoricActivity> listHistoricActivity(String workflowKey, Object businessKey);

}
