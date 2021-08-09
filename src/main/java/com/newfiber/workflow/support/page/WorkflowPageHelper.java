package com.newfiber.workflow.support.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newfiber.core.base.WorkflowPageReq;
import com.newfiber.workflow.support.IWorkflowCallback;

/**
 * 工作流分页工具类
 */
public class WorkflowPageHelper extends PageHelper {

    protected static final ThreadLocal<WorkflowPage> LOCAL_WORKFLOW_PAGE = new ThreadLocal<>();

    /**
     * 开始分页
     * @param pageNum 开始页数
     * @param pageSize 每页数量
     * @param orderBy 排序字段
     * @param userId 用户编号
     * @param taskKey 任务编号，对应相应的状态节点
     * @param workflowCallback 回调接口
     * @return 分页参数实体
     */
    public static <E> Page<E> startPage(int pageNum, int pageSize, String orderBy, Object userId, String taskKey, IWorkflowCallback<?> workflowCallback) {
        Page<E> page = startPage(pageNum, pageSize, orderBy);
        LOCAL_WORKFLOW_PAGE.set(WorkflowPage.build(userId, taskKey, workflowCallback));
        return page;
    }

    /**
     * 开始分页
     * @param workflowPageReq 分页接口参数
     * @param workflowCallback 回调接口
     * @return 分页参数实体
     */
    public static <E> Page<E> startPage(WorkflowPageReq workflowPageReq, IWorkflowCallback<?> workflowCallback) {
        Page<E> page = startPage(workflowPageReq.getPageNum(), workflowPageReq.getPageSize(), workflowPageReq.getOrderBy());
        LOCAL_WORKFLOW_PAGE.set(WorkflowPage.build(workflowPageReq.getWorkflowUserId(), workflowPageReq.getStatus(), workflowCallback));
        return page;
    }

    /**
     * 开始分页
     * @param userId 用户编号
     * @param taskKey 任务编号，对应相应的状态节点
     * @param workflowCallback 回调接口
     */
    public static void startPage(Object userId, String taskKey, IWorkflowCallback<?> workflowCallback) {
        LOCAL_WORKFLOW_PAGE.set(WorkflowPage.build(userId, taskKey, workflowCallback));
    }

    public static WorkflowPage getWorkflowPage(){
        return LOCAL_WORKFLOW_PAGE.get();
    }

    public static void clearPage() {
        LOCAL_WORKFLOW_PAGE.remove();
    }

}
