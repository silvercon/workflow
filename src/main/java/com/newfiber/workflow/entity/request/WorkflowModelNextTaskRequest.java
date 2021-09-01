package com.newfiber.workflow.entity.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowModelNextTaskRequest {

    /**
     * 工作流编号
     */
    @NotNull
    @ApiModelProperty(name = "workflowKey", value = "工作流编号", required = true)
    private String workflowKey;

    /**
     * 当前任务
     */
    @NotNull
    @ApiModelProperty(name = "currentTask", value = "当前任务", required = true)
    private String currentTask;

}
