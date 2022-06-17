package com.newfiber.workflow.enums;

/**
 * @author xiongkai
 */
public enum EQueryScope {

    /**
     * 查询范围
     */
    Todo("todo", "代办"),
	Done("done", "已办"),
	All("all", "全部"),

;

    public static EQueryScope match(String key){
	    for(EQueryScope queryScope : EQueryScope.values()){
	    	if(queryScope.key.equals(key)){
	    		return queryScope;
		    }
	    }
	    return EQueryScope.All;
    }

    EQueryScope(String key, String stringValue) {
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
