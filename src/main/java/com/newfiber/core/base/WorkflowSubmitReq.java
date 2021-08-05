package com.newfiber.core.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkflowSubmitReq {

    /**
     * 审核结果
     */
    @ApiModelProperty(name = "approveResult", value = "审核结果")
    String approveResult;

}
