package com.newfiber.workflow.entity.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowTodoBusinessExecutorListRequest {

    /**
     * 工作流编号
     */
    @NotNull
    @ApiModelProperty(name = "workflowKey", value = "工作流编号", required = true)
    private String workflowKey;

    /**
     * 业务编号
     */
    @NotNull
    @ApiModelProperty(name = "businessKey", value = "业务编号", required = true)
    private String businessKey;

}
