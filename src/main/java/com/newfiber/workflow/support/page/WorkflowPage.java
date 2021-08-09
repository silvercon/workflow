package com.newfiber.workflow.support.page;

import com.newfiber.workflow.support.IWorkflowCallback;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkflowPage {

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 任务主键/状态
     */
    private String taskKey;

    /**
     * 回调接口
     */
    private IWorkflowCallback<?> workflowCallback;

    public static WorkflowPage build(Object userId, String taskKey, IWorkflowCallback<?> workflowCallback){
        return new WorkflowPage(userId.toString(), taskKey, workflowCallback);
    }
}
