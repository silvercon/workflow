package com.newfiber.workflow.controller;

import com.newfiber.core.result.Result;
import com.newfiber.core.result.ResultCode;
import com.newfiber.workflow.entity.WorkflowGroup;
import com.newfiber.workflow.entity.WorkflowUser;
import com.newfiber.workflow.service.ActivitiUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "WF03-工作流用户管理", tags = "WF03-工作流用户管理")
@RequestMapping("/workflow-user")
public class WorkflowUserController {

    @Resource
    private ActivitiUserService activitiUserService;

    @ApiOperation(value = "列表查询用户")
    @PostMapping(value = "list-user")
    public Result<List<WorkflowUser>> listUser() {
        return new Result<>(ResultCode.SUCCESS, activitiUserService.listUser());
    }

    @ApiOperation(value = "列表查询用户组")
    @PostMapping(value = "list-group")
    public Result<List<WorkflowGroup>> listGroup() {
        return new Result<>(ResultCode.SUCCESS, activitiUserService.listGroup());
    }

}
