package com.newfiber.core.base;

import com.newfiber.workflow.support.IWorkflowCallback;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工作流任务提交请求，业务请求继承该类
 * @see com.newfiber.workflow.service.ActivitiProcessService#submitWorkflow(IWorkflowCallback, Object, WorkflowSubmitReq)
 */
@Data
public class WorkflowSubmitReq {

    /**
     * 审核结果，流程图变量${approveResult}
     */
    @NotNull
    @ApiModelProperty(name = "approveResult", value = "审核结果，流程图变量${approveResult}", required = true)
    String approveResult;

    /**
     * 提交人编号
     */
    @NotNull
    @ApiModelProperty(name = "submitUserId", value = "提交人编号", required = true)
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
