package com.newfiber.workflow.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkflowTodoBusinessKeyListRequest {

    /**
     * 用户编号
     */
    @ApiModelProperty(name = "userId", value = "用户编号")
    private String userId;

    /**
     * 用户组编号
     */
    @ApiModelProperty(name = "groupId", value = "用户组编号")
    private String groupId;

    /**
     * 工作流编号
     */
    @ApiModelProperty(name = "workflowKey", value = "工作流编号")
    private String workflowKey;

    /**
     * 任务编号
     */
    @ApiModelProperty(name = "taskKey", value = "任务编号")
    private String taskKey;

}
