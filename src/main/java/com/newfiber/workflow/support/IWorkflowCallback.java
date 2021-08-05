package com.newfiber.workflow.support;

import com.newfiber.workflow.enums.IWorkflowDefinition;

public interface IWorkflowCallback {

    IWorkflowDefinition getWorkflowDefinition();

    void refreshWorkflowInstanceId(Object businessKey, String workflowInstanceId);

    void refreshStatus(Object businessKey, String status);

}
