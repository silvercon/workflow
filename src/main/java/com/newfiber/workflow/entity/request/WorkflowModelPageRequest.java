package com.newfiber.workflow.entity.request;

import com.newfiber.core.base.BasePageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkflowModelPageRequest extends BasePageReq {

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "名称")
    private String name;

}
