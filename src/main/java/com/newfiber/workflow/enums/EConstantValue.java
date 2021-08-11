package com.newfiber.workflow.enums;

/**
 * @author xiongkai
 */
public enum EConstantValue {

    /**
     */
    ApproveResultField("审核结果字段", "approveResult"),
    ApproveUserField("审核用户字段", "approveUserId"),
    ApproveRoleField("审核角色字段", "approveRoleId"),

    EndEvent("结束事件", "end"),

    IWorkflowCallback("工作流回调接口", "IWorkflowCallback"),

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
