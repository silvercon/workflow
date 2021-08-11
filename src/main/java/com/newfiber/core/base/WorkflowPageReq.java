package com.newfiber.core.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流任务分页查询请求，业务请求继承该类
 * @see com.newfiber.workflow.service.ActivitiProcessService#pageTodoBusinessKey(String, String, Object, Object, WorkflowPageReq)
 */
@Data
public class WorkflowPageReq extends BasePageReq{

    /**
     * 工作流实例编号
     */
    @ApiModelProperty(name = "workflowInstanceId", value = "工作流实例编号")
    private String workflowInstanceId;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态")
    private String status;

    /**
     * 工作流用户编号(查询该用户的待办/已完成)
     */
    @ApiModelProperty(name = "workflowUserId", value = "工作流用户编号(查询该用户的待办/已完成)")
    private String workflowUserId;

}
