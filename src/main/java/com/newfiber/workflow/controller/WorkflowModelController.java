package com.newfiber.workflow.controller;

import com.newfiber.core.result.PageInfo;
import com.newfiber.core.result.Result;
import com.newfiber.core.result.ResultCode;
import com.newfiber.workflow.entity.WorkflowModel;
import com.newfiber.workflow.entity.request.WorkflowModeCreateRequest;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "WF02-工作流模型管理", tags = "WF02-工作流模型管理")
@RequestMapping("/workflow-model")
public class WorkflowModelController {

    @Resource
    private ActivitiModelService activitiModelService;

    @ApiOperation(value = "创建工作流模型")
    @PostMapping(value = "/create")
    public Result<Object> create(@RequestBody @Valid WorkflowModeCreateRequest request) {
        return new Result<>(ResultCode.SUCCESS, activitiModelService.create(request));
    }

    @ResponseBody
    @ApiOperation(value = "保存工作流模型")
    @PutMapping(value = "/save")
    public Object save(@RequestParam("modelId") String modelId, @RequestParam("key") String key, @RequestParam("name") String name,
            @RequestParam("jsonXml") String jsonXml, @RequestParam("svgXml") String svgXml,
            @RequestParam("description") String description) {
        activitiModelService.save(modelId, key, name, jsonXml, svgXml, description);
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
    public Result<Object> upload(@RequestParam String workflowKey, @RequestParam MultipartFile multipartFile) {
        activitiModelService.upload(workflowKey, multipartFile);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "下一步任务信息")
    @PostMapping(value = "/nextTasks")
    public Result<List<WorkflowModelNextTaskResponse>> nextTasks(@RequestBody @Valid WorkflowModelNextTaskRequest request) {
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

    /**
     *@Description: 实际项目中使用 activiti编辑的bpmn文件的部署
     *@author chenqian
     *@date 2022/1/24 13:23
     *@params String workflowKey, @RequestParam MultipartFile multipartFile
     *@return Result<Object>
     */
    @ApiOperation(value = "部署web工作流编辑器文件 bpmn文件")
    @PostMapping(value = "/deployWebActivitiServerFile")
    public Result<Object> deployWebActivitiServerFile(@RequestParam String workflowKey, @RequestParam MultipartFile multipartFile) {
        activitiModelService.deployWebActivitiServerFile(workflowKey, multipartFile);
        return new Result<>(ResultCode.SUCCESS);
    }

}
