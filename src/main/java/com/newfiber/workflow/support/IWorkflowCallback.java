package com.newfiber.workflow.support;

import com.newfiber.workflow.enums.IWorkflowDefinition;

public interface IWorkflowCallback {

    IWorkflowDefinition getWorkflowDefinition();

    <T> void refreshWorkflowInstanceId(T businessKey, String workflowInstanceId);

    <T> void refreshStatus(T businessKey, String status);

}
