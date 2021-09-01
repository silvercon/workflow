package com.newfiber.workflow.support;

/**
 * 工作流定义
 * @see com.newfiber.workflow.support.IWorkflowCallback#getWorkflowDefinition()
 */
public interface IWorkflowDefinition {

    /**
     * 工作流编号
     * @return 工作流编号
     */
    String getWorkflowKey();

    /**
     * 工作流名称
     * @return 工作流名称
     */
    String getWorkflowName();

    /**
     * 业务实体表名
     * @return 业务实体表名
     */
    default String getTableName() {
        return "t";
    }

    /**
     * 业务实体表主键类型
     * @return 业务实体表主键类型
     */
    default Class<?> getTableIdType(){
        return String.class;
    }
}
