package com.newfiber.workflow.service.impl;

import com.newfiber.workflow.enums.IWorkflowDefinition;
import com.newfiber.workflow.service.ActivitiProcessService;
import com.newfiber.workflow.support.IWorkflowCallback;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivitiProcessServiceImpl implements ActivitiProcessService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Override
    public <T> String startWorkflow(IWorkflowDefinition workflowDefinition, T businessKey, Map<String, Object> variables) {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(workflowDefinition.getWorkflowKey(),
                businessKey.toString(), variables);

        return processInstance.getProcessInstanceId();
    }

    @Override
    public <T> String startWorkflow(T businessKey, Map<String, Object> variables, IWorkflowCallback callback) {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(callback.getWorkflowDefinition().getWorkflowKey(),
                businessKey.toString(), variables);

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        callback.refreshStatus(businessKey, task.getTaskDefinitionKey());
        callback.refreshWorkflowInstanceId(businessKey, processInstance.getProcessInstanceId());

        return processInstance.getProcessInstanceId();
    }

    @Override
    public <T, E> String submitWorkflow(T businessKey, E submitUser, String approveResult, IWorkflowCallback callback) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(
                businessKey.toString(), callback.getWorkflowDefinition().getWorkflowKey()).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("approveResult", approveResult);

        taskService.claim(task.getId(), submitUser.toString());
        taskService.complete(task.getId(), variables);

        task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        if(null != task){
            callback.refreshStatus(businessKey, task.getTaskDefinitionKey());
        }

        return null;
    }
}
