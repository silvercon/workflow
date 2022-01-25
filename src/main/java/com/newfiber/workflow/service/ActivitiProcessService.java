package com.newfiber.workflow.service;

import com.newfiber.core.result.PageInfo;
import com.newfiber.workflow.entity.WorkflowHistoricActivity;
import com.newfiber.workflow.entity.WorkflowUser;
import com.newfiber.workflow.support.IWorkflowCallback;
import com.newfiber.workflow.support.request.WorkflowPageReq;
import com.newfiber.workflow.support.request.WorkflowStartReq;
import com.newfiber.workflow.support.request.WorkflowSubmitReq;
import java.util.List;
import java.util.Map;

/**
 * 工作流过程
 */
public interface ActivitiProcessService {

    // ************************* 启动工作流 ************************* //

    /**
     * 启动工作流
     * @param workflowCallback 回调接口
     * @param businessKey 业务编号
     * @return 工作流实体编号
     */
    String startWorkflow(IWorkflowCallback<?> workflowCallback, Object businessKey);

    /**
     * 启动工作流
     * @param workflowCallback 回调接口
     * @param businessKey 业务编号
     * @param startReq 启动参数
     * @return 工作流实体编号
     */
    String startWorkflow(IWorkflowCallback<?> workflowCallback, Object businessKey, WorkflowStartReq startReq);

    /**
     * 启动工作流
     * @param workflowCallback 回调接口
     * @param businessKey 业务编号
     * @param variables 工作流变量
     * @return 工作流实体编号
     */
    String startWorkflow(IWorkflowCallback<?> workflowCallback, Object businessKey, Map<String, Object> variables);

    // ************************* 提交工作流 ************************* //

    /**
     * 提交工作流
     * @param callback 回调接口
     * @param businessKey 业务编号
     * @param submitReq 提交结果
     * @return 业务编号
     */
    String submitWorkflow(IWorkflowCallback<?> callback, Object businessKey, WorkflowSubmitReq submitReq);

    /**
     * 提交工作流
     * @param callback 回调接口
     * @param businessKey 业务编号
     * @param submitUser 提交人
     * @param approveResult 提交结果
     * @param nextTaskApproveUserId 下一步审核人
     * @param nextTaskApproveUserIdList 下一步任务会签审核人（会签）
     * @param nextTaskApproveRoleId 下一步审核角色
     * @param notificationTemplateArgs 消息模板参数
     * @return 业务编号
     */
    String submitWorkflow(IWorkflowCallback<?> callback, Object businessKey, Object submitUser, String approveResult,
            String nextTaskApproveUserId, List<String> nextTaskApproveUserIdList, String nextTaskApproveRoleId,
            List<String> notificationTemplateArgs);

    // ************************* 消息通知 ************************* //

    /**
     * 发送邮件通知
     * @param email 邮箱
     * @param content 内容
     * @return 是否成功
     */
    boolean sendEmailNotification(String email, String content);

    /**
     * 发送短信通知
     * @param mobile 手机号
     * @param smsSign 短信签名
     * @param smsTemplateCode 短息模板编号
     * @param templateArgs 模板桉树
     * @return 是否成功
     */
    boolean sendSmsNotification(String mobile, String smsSign, String smsTemplateCode, List<String> templateArgs);

    // ************************* 根据用户列表查询工作流 ************************* //

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
     * 查询任务待办/已完成的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listInvolvedBusinessKeyByUser(String workflowKey, String taskKey, Object userId);

    /**
     * 根据用户查询代办的业务实体编号
     * @param callback 回调接口
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listTodoBusinessKeyByUser(IWorkflowCallback<?> callback, String taskKey, Object userId);

    /**
     * 查询任务已完成的业务实体编号
     * @param callback 回调接口
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listTaskDoneBusinessKeyByUser(IWorkflowCallback<?> callback, String taskKey, Object userId);

    /**
     * 查询任务待办/已完成的业务实体编号
     * @param callback 回调接口
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @return 代办的业务实体编号
     */
    List<String> listInvolvedBusinessKeyByUser(IWorkflowCallback<?> callback, String taskKey, Object userId);

    // ************************* 根据用户/用户组列表查询工作流 ************************* //

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
     * 列表查询待办业务的可执行人
     * @param workflowKey 工作流编号
     * @param businessKey 业务编号
     * @return 工作流用户
     */
    List<WorkflowUser> listTodoBusinessExecutor(String workflowKey, Object businessKey);

    // ************************* 分页查询工作流 ************************* //

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

    // ************************* 列表查询工作流历史记录 ************************* //

    /**
     * 根据用户分页查询已办的业务实体编号
     * @param workflowKey 工作流编号
     * @param taskKey 任务编号
     * @param userId 用户编号
     * @param workflowPageReq 分页参数
     * @return 代办的业务实体编号
     */
    PageInfo<String> pageDoneBusinessKeyByUser(String workflowKey, String taskKey, Object userId, WorkflowPageReq workflowPageReq);

    /**
     * 列表查询历史活动记录
     * @param workflowKey 工作流编号
     * @param businessKey 业务实体编号
     * @return 历史活动记录
     */
    List<WorkflowHistoricActivity> listHistoricActivity(String workflowKey, Object businessKey, String workflowUserId, String status);

}
