package com.newfiber.workflow.service;

import com.newfiber.core.base.WorkflowPageReq;
import com.newfiber.core.base.WorkflowSubmitReq;
import com.newfiber.core.result.PageInfo;
import com.newfiber.workflow.entity.WorkflowHistoricActivity;
import com.newfiber.workflow.support.IWorkflowCallback;
import java.util.List;
import java.util.Map;

/**
 * 工作流过程
 */
public interface ActivitiProcessService {

    /**
     * 启动工作流
     * @param businessKey 业务编号
     * @param workflowCallback 回调接口
     * @return 工作流实体编号
     */
    String startWorkflow(Object businessKey, IWorkflowCallback<?> workflowCallback);

    /**
     * 启动工作流
     * @param businessKey 业务编号
     * @param workflowCallback 回调接口
     * @param variables 工作流变量
     * @return 工作流实体编号
     */
    String startWorkflow(Object businessKey, IWorkflowCallback<?> workflowCallback, Map<String, Object> variables);

    /**
     * 提交工作流
     * @param businessKey 业务编号
     * @param submitUser 提交人
     * @param approveResult 提交结果
     * @param callback 回调接口
     * @return TODO
     */
    String submitWorkflow(Object businessKey, Object submitUser, String approveResult, IWorkflowCallback<?> callback);

    /**
     * 提交工作流
     * @param businessKey 业务编号
     * @param submitUser 提交人
     * @param submitReq 提交结果
     * @param callback 回调接口
     * @return TODO
     */
    String submitWorkflow(Object businessKey, Object submitUser, WorkflowSubmitReq submitReq, IWorkflowCallback<?> callback);

    /**
     * 根据用户查询代办的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId);

    /**
     * 查询任务已完成的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listTaskDoneBusinessKeyByUser(String workflowKey, String taskKey, Object userId);

    /**
     * 根据用户查询涉及（待办/已完成）到的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listInvolvedBusinessKeyByUser(String workflowKey, String taskKey, Object userId);

    /**
     * 查询代办的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param groupId 用户组编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId);

    /**
     * 根据用户查询代办的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @param workflowPageReq 分页参数
     * @return 代办的业务实体编号
     */
    PageInfo<String> pageTodoBusinessKeyByUser(String workflowKey, String taskKey, Object userId, WorkflowPageReq workflowPageReq);

    /**
     * 查询代办的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param groupId 用户组编号
     * @param userId 用户编号
     * @param workflowPageReq 分页参数
     * @return 代办的业务实体编号
     */
    PageInfo<String> pageTodoBusinessKey(String workflowKey, String taskKey, Object groupId, Object userId, WorkflowPageReq workflowPageReq);

    /**
     * 列表查询历史活动记录
     * @param workflowKey 工作流编号
     * @param businessKey 业务实体编号
     * @return 历史活动记录
     */
    List<WorkflowHistoricActivity> listHistoricActivity(String workflowKey, Object businessKey, String workflowUserId, String status);

}
