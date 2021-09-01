package com.newfiber.workflow.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import org.activiti.engine.history.HistoricActivityInstance;
import org.springframework.beans.BeanUtils;

@Data
public class WorkflowHistoricActivity implements HistoricActivityInstance {

    /**
     * 编号
     */
    @ApiModelProperty(name = "id", value = "编号")
    private String id;

    /**
     * 活动编号
     */
    @ApiModelProperty(name = "activityId", value = "活动编号")
    private String activityId;

    /**
     * 活动名称
     */
    @ApiModelProperty(name = "activityName", value = "活动名称")
    private String activityName;

    /**
     * 活动类型
     */
    @ApiModelProperty(name = "activityType", value = "活动类型")
    private String activityType;

    /**
     * 流程定义编号
     */
    @ApiModelProperty(name = "processDefinitionId", value = "流程定义编号")
    private String processDefinitionId;

    /**
     * 流程实例编号
     */
    @ApiModelProperty(name = "processInstanceId", value = "流程实例编号")
    private String processInstanceId;

    /**
     * 执行编号
     */
    @ApiModelProperty(name = "executionId", value = "执行编号")
    private String executionId;

    /**
     * 任务编号
     */
    @ApiModelProperty(name = "taskId", value = "任务编号")
    private String taskId;

    /**
     * 被调用流程实例编号
     */
    @ApiModelProperty(name = "calledProcessInstanceId", value = "被调用流程实例编号")
    private String calledProcessInstanceId;

    /**
     * 分配人
     */
    @ApiModelProperty(name = "assignee", value = "分配人")
    private String assignee;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间")
    private Date endTime;

    /**
     * 持续时间
     */
    @ApiModelProperty(name = "durationInMillis", value = "持续时间")
    private Long durationInMillis;

    /**
     * 删除原因
     */
    @ApiModelProperty(name = "deleteReason", value = "删除原因")
    private String deleteReason;

    /**
     * 租户编号
     */
    @ApiModelProperty(name = "tenantId", value = "租户编号")
    private String tenantId;

    /**
     * 时间
     */
    @ApiModelProperty(name = "time", value = "时间")
    private Date time;

    public static WorkflowHistoricActivity build(HistoricActivityInstance historicActivityInstance){
        WorkflowHistoricActivity workflowHistoricActivity = new WorkflowHistoricActivity();
        BeanUtils.copyProperties(historicActivityInstance, workflowHistoricActivity);
        return workflowHistoricActivity;
    }
}
