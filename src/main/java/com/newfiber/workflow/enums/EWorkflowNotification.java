package com.newfiber.workflow.enums;

import com.newfiber.workflow.support.notification.IWorkflowEmailNotification;

public enum EWorkflowNotification implements IWorkflowEmailNotification {
    /**
     *
     */
    Default("[%s]您有一条待办任务:[%s]");

    private final String notificationTemplate;

    EWorkflowNotification(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }

    @Override
    public String getNotificationTemplate() {
        return notificationTemplate;
    }

}
