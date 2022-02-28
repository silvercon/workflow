package com.newfiber.workflow.support.listener;

import com.newfiber.workflow.enums.EConstantValue;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateTask;
import org.flowable.engine.delegate.TaskListener;

/**
 * 计数签任务监听器
 */
public class WorkflowCounterSignTaskListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {

        //获取流程id
        String exId = delegateTask.getExecutionId();
        //获取流程参数pass，会签人员完成自己的审批任务时会添加流程参数pass，false为拒绝，true为同意
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        boolean signCompletionCondition = (Boolean) runtimeService.getVariable(exId, EConstantValue.SignCompletionCondition.getValue());
        /*
         * false：有一个人拒绝，整个流程就结束了，
         * 	因为Complete condition的值为pass == false，即，当流程参数为pass时会签就结束开始下一个任务
         * 	所以，当pass == false时，直接设置下一个流程跳转需要的参数
         * true：审批人同意，同时要判断是不是所有的人都已经完成了，而不是由一个人同意该会签就结束
         * 	值得注意的是如果一个审批人完成了审批进入到该监听时nrOfCompletedInstances的值还没有更新，因此需要+1
         */
        Object approveResult = runtimeService.getVariable(exId, EConstantValue.ApproveResultField.getValue());
        if(signCompletionCondition){
            Integer complete = (Integer) runtimeService.getVariable(exId, "nrOfCompletedInstances");
            Integer all = (Integer) runtimeService.getVariable(exId, "nrOfInstances");
            //说明都完成了并且没有人拒绝
            if((complete + 1) / all == 1){
                runtimeService.setVariable(exId, EConstantValue.ApproveResultField.getValue(), approveResult);
            }
        }else{
            //会签结束，设置参数result为 false，下个任务为 上一节点 科领导审核
            runtimeService.setVariable(exId, EConstantValue.ApproveResultField.getValue(), approveResult);
        }
    }

}
