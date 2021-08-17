package com.newfiber.workflow.controller;

import com.newfiber.core.result.PageInfo;
import com.newfiber.core.result.Result;
import com.newfiber.core.result.ResultCode;
import com.newfiber.workflow.entity.WorkflowModel;
import com.newfiber.workflow.entity.request.WorkflowModeCreateRequest;
import com.newfiber.workflow.entity.request.WorkflowModeModifyRequest;
import com.newfiber.workflow.entity.request.WorkflowModelNextTaskRequest;
import com.newfiber.workflow.entity.request.WorkflowModelPageRequest;
import com.newfiber.workflow.entity.response.WorkflowModelNextTaskResponse;
import com.newfiber.workflow.service.ActivitiModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "WF01-工作流模型管理", tags = "WF01-工作流模型管理")
@RequestMapping("/workflow-model")
public class WorkflowModelController {

    @Resource
    private ActivitiModelService activitiModelService;

    @ApiOperation(value = "新增工作流模型")
    @PostMapping(value = "/create")
    public Result<Object> create(@RequestBody @Valid WorkflowModeCreateRequest request) {
        activitiModelService.create(request);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "修改工作流模型")
    @PostMapping(value = "/modify")
    public Result<Object> modify(@RequestBody @Valid WorkflowModeModifyRequest request) {
        activitiModelService.modify(request);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "部署工作流模型")
    @PostMapping(value = "/deploy/{modelId}")
    public Result<Object> deploy(@PathVariable String modelId) {
        activitiModelService.deploy(modelId);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "删除工作流模型")
    @PostMapping(value = "/delete/{modelId}")
    public Result<Object> delete(@PathVariable String modelId) {
        activitiModelService.delete(modelId);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "上传工作流文件")
    @PostMapping(value = "/upload")
    public Result<Object> upload(@RequestParam MultipartFile multipartFile) {
        activitiModelService.upload(multipartFile);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "下一步网关任务信息")
    @PostMapping(value = "/nextGatewayTasks")
    public Result<List<WorkflowModelNextTaskResponse>> nextGatewayTasks(@RequestBody @Valid WorkflowModelNextTaskRequest request) {
        return new Result<>(ResultCode.SUCCESS, activitiModelService
                .nextTasks(request.getWorkflowKey(), request.getCurrentTask()));
    }

    @ApiOperation(value = "详细查询工作流模型")
    @PostMapping(value = "/detail/{modelId}")
    public Result<WorkflowModel> detail(@PathVariable String modelId) {
        return new Result<>(ResultCode.SUCCESS, activitiModelService.detail(modelId));
    }

    @ApiOperation(value = "分页查询工作流模型")
    @PostMapping(value = "/page")
    public Result<PageInfo<WorkflowModel>> page(@RequestBody @Valid WorkflowModelPageRequest request) {
        return new Result<>(ResultCode.SUCCESS, activitiModelService.pageWorkflowModel(request));
    }

}
