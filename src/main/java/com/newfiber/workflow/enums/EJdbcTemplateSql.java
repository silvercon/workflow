package com.newfiber.workflow.enums;

/**
 * @author xiongkai
 */
public enum EJdbcTemplateSql {


;

    EJdbcTemplateSql(String key, String value) {
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
