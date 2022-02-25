package com.newfiber.workflow.support.page;

import com.newfiber.workflow.support.IWorkflowCallback;
import io.swagger.annotations.ApiModelProperty;
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
	 * 查询范围（EQueryScope）
	 */
	private String queryScope;

    /**
     * 回调接口
     */
    private IWorkflowCallback<?> workflowCallback;

    public static WorkflowPage build(Object userId, String taskKey, String queryScope, IWorkflowCallback<?> workflowCallback){
        return new WorkflowPage(userId.toString(), taskKey, queryScope, workflowCallback);
    }
}
