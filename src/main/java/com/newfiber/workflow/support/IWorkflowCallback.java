package com.newfiber.workflow.support;

import com.newfiber.workflow.utils.ReflectionKit;

/**
 * 工作流回调接口，业务service实现该接口
 * @see com.newfiber.workflow.service.ActivitiProcessService
 * @param <T> 业务实体
 */
public interface IWorkflowCallback<T> {

    /**
     * 业务实体类型
     * @return 业务实体类型
     */
    default Class<?> getEntityClass(){
        return ReflectionKit.getInterfaceGeneric(this);
    }

    /**
     * 工作流定义
     * @return 工作流定义
     */
    IWorkflowDefinition getWorkflowDefinition();

    /**
     * 更新业务实体的工作流实体编号
     * @param businessKey 业务实体编号
     * @param workflowInstanceId 工作流实体编号
     */
    void refreshWorkflowInstanceId(Object businessKey, String workflowInstanceId);

    /**
     * 更新业务数据状态
     * @param businessKey 业务实体编号
     * @param status 状态
     */
    void refreshStatus(Object businessKey, String status);

}
