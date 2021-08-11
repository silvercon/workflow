package com.newfiber.core.base;

import com.newfiber.workflow.support.IWorkflowCallback;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流任务开始请求，业务请求继承该类
 * @see com.newfiber.workflow.service.ActivitiProcessService#startWorkflow(IWorkflowCallback, Object, WorkflowStartReq)
 */
@Data
public class WorkflowStartReq {

    /**
     * 提交人编号
     */
    @ApiModelProperty(name = "submitUserId", value = "提交人编号", hidden = true)
    private String submitUserId;

    /**
     * 下一步任务审核人
     */
    @ApiModelProperty(name = "nextTaskApproveUserId", value = "下一步任务审核人")
    private String nextTaskApproveUserId;

    /**
     * 下一步任务审核角色
     */
    @ApiModelProperty(name = "nextTaskApproveRoleId", value = "下一步任务审核角色")
    private String nextTaskApproveRoleId;

}
