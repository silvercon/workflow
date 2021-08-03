package com.newfiber.workflow.service;

import com.newfiber.workflow.enums.IWorkflowDefinition;
import com.newfiber.workflow.support.IWorkflowCallback;
import java.util.Map;

public interface ActivitiProcessService {

    <T> String startWorkflow(IWorkflowDefinition workflowDefinition, T businessKey, Map<String, Object> variables);

    <T> String startWorkflow(T businessKey, Map<String, Object> variables, IWorkflowCallback callback);

    <T, E> String submitWorkflow(T businessKey, E submitUser, String approveResult, IWorkflowCallback callback);


}
