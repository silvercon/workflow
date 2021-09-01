package com.newfiber.workflow.enums;

/**
 * @author xiongkai
 */
public enum EBoolean {

    /**
     */
    False(false, "false"),
    True(true, "true"),

;

    EBoolean(boolean key, String stringValue) {
        this.key = key;
        this.stringValue = stringValue;
    }

    private final boolean key;

    private final String stringValue;

    public boolean getKey() {
        return key;
    }

    public String getStringValue() {
        return stringValue;
    }
}
