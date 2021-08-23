package com.newfiber.workflow.entity.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowModeSaveRequest {

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
     * 流程定义json
     */
    @ApiModelProperty(name = "jsonXml", value = "流程定义json")
    private String jsonXml;

    /**
     * 流程定义svg
     */
    @ApiModelProperty(name = "svgXml", value = "流程定义svg")
    private String svgXml;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "描述")
    private String description;

}
