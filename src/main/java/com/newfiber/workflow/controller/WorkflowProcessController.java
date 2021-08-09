package com.newfiber.workflow.controller;

import com.newfiber.core.result.Result;
import com.newfiber.core.result.ResultCode;
import com.newfiber.workflow.entity.WorkflowHistoricActivity;
import com.newfiber.workflow.entity.request.WorkflowHistoryActivityListRequest;
import com.newfiber.workflow.entity.request.WorkflowTodoBusinessKeyListRequest;
import com.newfiber.workflow.service.ActivitiProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "WF03-工作流流程管理", tags = "WF03-工作流流程管理")
@RequestMapping("/workflow-process")
public class WorkflowProcessController {

    @Resource
    private ActivitiProcessService activitiProcessService;

    @ApiOperation(value = "列表查询待办业务编号")
    @PostMapping(value = "list-todo-businessKey")
    public Result<List<String>> listTodoBusinessKey(@Valid @RequestBody WorkflowTodoBusinessKeyListRequest request) {
        return new Result<>(ResultCode.SUCCESS, activitiProcessService.listTodoBusinessKey(
                request.getUserId(), request.getGroupId(), request.getWorkflowKey(), request.getTaskKey()));
    }

    @ApiOperation(value = "列表查询历史活动记录")
    @PostMapping(value = "list-history-activity")
    public Result<List<WorkflowHistoricActivity>> listHistoryActivity(@Valid @RequestBody WorkflowHistoryActivityListRequest request) {
        return new Result<>(ResultCode.SUCCESS, activitiProcessService.listHistoricActivity(
                request.getWorkflowKey(), request.getBusinessKey(), request.getWorkflowUserId(), request.getStatus()));
    }

}
