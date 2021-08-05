package com.newfiber.workflow.support.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newfiber.workflow.support.IWorkflowCallback;

public class WorkflowPageHelper extends PageHelper {

    protected static final ThreadLocal<WorkflowPage> LOCAL_WORKFLOW_PAGE = new ThreadLocal<>();

    public static <E> Page<E> startPage(int pageNum, int pageSize, String orderBy, Object userId, String taskKey, IWorkflowCallback workflowCallback) {
        Page<E> page = startPage(pageNum, pageSize, orderBy);
        LOCAL_WORKFLOW_PAGE.set(WorkflowPage.build(userId, taskKey, workflowCallback));
        return page;
    }

    public static void startPage(Object userId, String taskKey, IWorkflowCallback workflowCallback) {
        LOCAL_WORKFLOW_PAGE.set(WorkflowPage.build(userId, taskKey, workflowCallback));
    }

    public static WorkflowPage getWorkflowPage(){
        return LOCAL_WORKFLOW_PAGE.get();
    }
}
