package com.newfiber.workflow.entity.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.Task;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowModelNextTaskResponse {

    /**
     * 下一个任务编号
     */
    @ApiModelProperty(name = "nextTaskKey", value = "下一个任务编号")
    private String nextTaskKey;

    /**
     * 下一个任务名称
     */
    @ApiModelProperty(name = "nextTaskName", value = "下一个任务名称")
    private String nextTaskName;

    /**
     * 条件表达式
     */
    @ApiModelProperty(name = "conditionExpression", value = "条件表达式")
    private String conditionExpression;

    public WorkflowModelNextTaskResponse(String nextTaskKey, String nextTaskName) {
        this.nextTaskKey = nextTaskKey;
        this.nextTaskName = nextTaskName;
    }

    public static void append(List<WorkflowModelNextTaskResponse> responses, Task task, String conditionExpression){
        responses.add(new WorkflowModelNextTaskResponse(task.getId(), task.getName(), conditionExpression));
    }

    public static void append(List<WorkflowModelNextTaskResponse> responses, List<FlowElement> flowElementList){
        flowElementList.forEach(t -> responses.add(new WorkflowModelNextTaskResponse(((SequenceFlow) t).getTargetRef(), t.getName(), ((SequenceFlow) t).getConditionExpression())));
    }

}
