package com.newfiber.workflow.support.notification;

public interface IWorkflowEmailNotification extends IWorkflowNotification{

    /**
     * 通知模板
     * @return 通知模板
     */
    String getNotificationTemplate();

}
