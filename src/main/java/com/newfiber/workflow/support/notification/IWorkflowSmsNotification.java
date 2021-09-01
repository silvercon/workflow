package com.newfiber.workflow.support.notification;

public interface IWorkflowSmsNotification extends IWorkflowNotification{

    /**
     * 短信签名
     * @return 短信签名
     */
    default String getSmsSign(){
        return null;
    }

    /**
     * 短信模板编号
     * @return 短信模板编号
     */
    default String getSmsTemplateCode(){
        return null;
    }

}
