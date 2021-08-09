package com.newfiber.core.base;

import com.newfiber.workflow.support.IWorkflowCallback;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流任务提交请求，业务请求继承该类
 * @see com.newfiber.workflow.service.ActivitiProcessService#submitWorkflow(Object, Object, WorkflowSubmitReq, IWorkflowCallback) 
 */
@Data
public class WorkflowSubmitReq {

    /**
     * 审核结果
     */
    @ApiModelProperty(name = "approveResult", value = "审核结果")
    String approveResult;

}
