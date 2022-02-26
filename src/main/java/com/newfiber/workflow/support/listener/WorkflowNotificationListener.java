package com.newfiber.workflow.support.listener;

import cn.hutool.core.util.ReflectUtil;
import com.newfiber.workflow.enums.EConstantValue;
import com.newfiber.workflow.enums.EUserInfo;
import com.newfiber.workflow.enums.EWorkflowNotification;
import com.newfiber.workflow.service.ActivitiCommonService;
import com.newfiber.workflow.service.ActivitiProcessService;
import com.newfiber.workflow.support.IWorkflowCallback;
import com.newfiber.workflow.support.notification.IWorkflowEmailNotification;
import com.newfiber.workflow.support.notification.IWorkflowNotification;
import com.newfiber.workflow.support.notification.IWorkflowSmsNotification;
import com.newfiber.workflow.utils.ApplicationContextProvider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateTask;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.flowable.idm.api.User;
import org.springframework.util.CollectionUtils;

/**
 * 通知监听器
 */
@Slf4j
public class WorkflowNotificationListener implements TaskListener {

    private TaskService taskService;

    private ActivitiCommonService activitiCommonService;

    private IdentityService identityService;

    private ActivitiProcessService activitiProcessService;

    private RuntimeService runtimeService;

    {
        resourceCheck();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void notify(DelegateTask execution) {
        Set<String> taskUserIdSet = new HashSet<>();
        if(StringUtils.isNotBlank(execution.getAssignee())){
            taskUserIdSet.add(execution.getAssignee());
        }

        Set<String> taskCandidatesUserIdSet = activitiCommonService.listTaskUserId(execution.getCandidates());
        if(!CollectionUtils.isEmpty(taskCandidatesUserIdSet)){
            taskUserIdSet.addAll(taskCandidatesUserIdSet);
        }

        if(CollectionUtils.isEmpty(taskUserIdSet)){
            return;
        }

        Object callbackObject = execution.getVariable(EConstantValue.IWorkflowCallback.getValue());
        IWorkflowCallback<?> workflowCallback = (IWorkflowCallback<?>) ApplicationContextProvider.
                getBean(ReflectUtil.newInstance(callbackObject.toString()).getClass());

        IWorkflowNotification workflowNotification;
        IWorkflowNotification[] workflowNotifications = workflowCallback.getWorkflowNotification();
        if(null == workflowNotifications){
            workflowNotification = EWorkflowNotification.Default;
        }else{
            workflowNotification = getWorkflowNotification(execution, workflowNotifications);
        }

        // 模板参数
        List<String> notificationTemplateArgs = new ArrayList<>();
        Object notificationTemplateArgsObject = execution.getTransientVariable(EConstantValue.NotificationTemplateArgs.getValue());
        if(notificationTemplateArgsObject instanceof List){
            notificationTemplateArgs = (List<String>) notificationTemplateArgsObject;
        }

        for(String userId : taskUserIdSet){
            if(workflowNotification instanceof IWorkflowEmailNotification){
                User user = identityService.createUserQuery().userId(userId).singleResult();
                String content = parseEmailContent(execution, workflowCallback, (IWorkflowEmailNotification) workflowNotification, notificationTemplateArgs);
                activitiProcessService.sendEmailNotification(user.getEmail(), content);
            }

            if(workflowNotification instanceof IWorkflowSmsNotification){
                String mobile = identityService.getUserInfo(userId, EUserInfo.Mobile.getKey());
                IWorkflowSmsNotification workflowSmsNotification = (IWorkflowSmsNotification) workflowNotification;
                activitiProcessService.sendSmsNotification(mobile, workflowSmsNotification.getSmsSign(), workflowSmsNotification.getSmsTemplateCode(), notificationTemplateArgs);
            }
        }

    }

    private String parseEmailContent(DelegateTask execution, IWorkflowCallback<?> workflowCallback,
            IWorkflowEmailNotification workflowNotification, List<String> notificationTemplateArgs) {
        String content = workflowNotification.getNotificationTemplate();
        if(EWorkflowNotification.Default.equals(workflowNotification)){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(
                    execution.getProcessInstanceId()).singleResult();
            notificationTemplateArgs.add(workflowCallback.getWorkflowDefinition().getWorkflowName());
            notificationTemplateArgs.add(processInstance.getBusinessKey());
        }

        return String.format(content, notificationTemplateArgs.toArray());
    }

    private IWorkflowNotification getWorkflowNotification(DelegateTask execution, IWorkflowNotification[] workflowNotifications) {
        IWorkflowNotification workflowNotification = EWorkflowNotification.Default;
        for(IWorkflowNotification notification : workflowNotifications){
            if(notification.getNotificationTask().equals(execution.getTaskDefinitionKey())){
                workflowNotification = notification;
            }
        }
        return workflowNotification;
    }

    synchronized private void resourceCheck(){
        if(null == taskService){
            taskService = ApplicationContextProvider.getBean(TaskService.class);
        }
        if(null == activitiCommonService){
            activitiCommonService = ApplicationContextProvider.getBean(ActivitiCommonService.class);
        }
        if(null == identityService){
            identityService = ApplicationContextProvider.getBean(IdentityService.class);
        }
        if(null == activitiProcessService){
            activitiProcessService = ApplicationContextProvider.getBean(ActivitiProcessService.class);
        }
        if(null == runtimeService){
            runtimeService = ApplicationContextProvider.getBean(RuntimeService.class);
        }
    }

}
