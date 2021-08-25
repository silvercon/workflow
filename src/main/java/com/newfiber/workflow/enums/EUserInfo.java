package com.newfiber.workflow.enums;

/**
 * @author xiongkai
 */
public enum EUserInfo {

    /**
     */
    Mobile("mobile", "手机号"),

;

    EUserInfo(String key, String stringValue) {
        this.key = key;
        this.stringValue = stringValue;
    }

    private final String key;

    private final String stringValue;

    public String getKey() {
        return key;
    }

    public String getStringValue() {
        return stringValue;
    }
}
