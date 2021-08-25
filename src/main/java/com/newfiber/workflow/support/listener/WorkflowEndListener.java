package com.newfiber.workflow.support.listener;

import cn.hutool.core.util.ReflectUtil;
import com.newfiber.workflow.enums.EConstantValue;
import com.newfiber.workflow.support.IWorkflowCallback;
import com.newfiber.workflow.utils.ApplicationContextProvider;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 执行流监听器
 */
public class WorkflowEndListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {

        // 更新结束状态
        if(EConstantValue.EndEvent.getValue().equals(execution.getCurrentActivityId())){
            Object callbackObject = execution.getVariable(EConstantValue.IWorkflowCallback.getValue());
            IWorkflowCallback<?> workflowCallback = (IWorkflowCallback<?>) ApplicationContextProvider.
                    getBean(ReflectUtil.newInstance(callbackObject.toString()).getClass());
            workflowCallback.refreshStatus(execution.getProcessInstanceBusinessKey(), EConstantValue.EndEvent.getValue());
        }

    }

}
