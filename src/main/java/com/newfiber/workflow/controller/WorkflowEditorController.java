package com.newfiber.workflow.controller;

import com.newfiber.workflow.service.ActivitiEditorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "WF01-工作流图形编辑器管理", tags = "WF01-工作流图形编辑器管理")
@RequestMapping("/workflow-editor")
public class WorkflowEditorController {

    @Resource
    private ActivitiEditorService activitiEditorService;

    @ApiOperation(value = "查询BPMN按钮")
    @GetMapping(value = "/stencilset")
    public Object stencilset() {
        return activitiEditorService.stencilset();
    }

}
