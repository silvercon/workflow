package com.newfiber.workflow.enums;

/**
 * @author xiongkai
 */
public enum EConstantValue {

    /**
     */
    ApproveResultField("审核结果字段", "approveResult"),

    ApproveUserIdField("审核用户字段", "approveUserId"),
    ApproveUserIdListField("审核用户（会签）字段", "approveUserIdList"),
    ApproveRoleIdField("审核角色字段", "approveRoleId"),
    NotificationTemplateArgs("消息模板参数", "notificationTemplateArgs"),

    EndEvent("结束事件", "end"),

    IWorkflowCallback("工作流回调接口", "IWorkflowCallback"),

    EmailNotificationSubject("邮件消息通知主题", "提醒：您有新的待办任务"),

;

    EConstantValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private final String key;

    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
