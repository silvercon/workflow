package com.newfiber.workflow.support.notification;

public interface IWorkflowNotification {

    /**
     * 通知的节点，默认通知已配置监听器的节点（状态）
     * @return 通知的节点，默认通知已配置监听器的节点（状态）
     */
    default String getNotificationTask(){
        return null;
    }

}
