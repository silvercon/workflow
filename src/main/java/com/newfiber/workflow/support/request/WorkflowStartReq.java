package com.newfiber.workflow.support.request;

import com.newfiber.workflow.support.IWorkflowCallback;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工作流任务开始请求，业务请求继承该类
 * @see com.newfiber.workflow.service.ActivitiProcessService#startWorkflow(IWorkflowCallback, Object, WorkflowStartReq)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
     * 下一步任务会签审核人（会签）
     */
    @ApiModelProperty(name = "nextTaskApproveUserIdList", value = "下一步任务会签审核人")
    private List<String> nextTaskApproveUserIdList;

    /**
     * 下一步任务审核角色
     */
    @ApiModelProperty(name = "nextTaskApproveRoleId", value = "下一步任务审核角色")
    private String nextTaskApproveRoleId;

    /**
     * 消息模板参数
     */
    @ApiModelProperty(name = "notificationTemplateArgs", value = "消息模板参数")
    private List<String> notificationTemplateArgs;

}
