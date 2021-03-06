package com.newfiber.workflow.entity.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowModeCreateRequest {

    /**
     * 关键字
     */
    @NotNull
    @ApiModelProperty(name = "key", value = "关键字", required = true)
    private String key;

    /**
     * 名称
     */
    @NotNull
    @ApiModelProperty(name = "name", value = "名称", required = true)
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "描述")
    private String description;

}
