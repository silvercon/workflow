package com.newfiber.core.base;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工作流任务分页查询请求，业务请求继承该类
 * @see com.newfiber.workflow.service.ActivitiProcessService#pageTodoBusinessKey(String, String, Object, Object, WorkflowPageReq)
 */
@Data
public class WorkflowPageReq {

    /**
     * 起始页
     */
    @ApiModelProperty(name = "pageNum", value = "起始页")
    @NotNull(message = "起始页不能为空")
    @Min(value = 0, message = "起始页要大于0")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(name = "pageSize", value = "每页数量")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 0, message = "每页数量要大于0")
    private Integer pageSize;

    /**
     * 排序字段
     */
    @ApiModelProperty(name = "orderBy", value = "排序字段")
    private String orderBy;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态")
    private String status;

    /**
     * 工作流用户编号
     */
    @ApiModelProperty(name = "workflowUserId", value = "工作流用户编号")
    private String workflowUserId;

    public Integer pageStart(){
        return pageNum * pageSize;
    }
}
