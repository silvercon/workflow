package com.newfiber.workflow.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkflowModel {
    /**
     * 编号
     */
    @ApiModelProperty(name = "id", value = "编号")
    private String id;

    /**
     * 部署编号
     */
    @ApiModelProperty(name = "deploymentId", value = "部署编号")
    private String deploymentId;

    /**
     * 版本
     */
    @ApiModelProperty(name = "version", value = "版本")
    private Integer version;

    /**
     * 分类
     */
    @ApiModelProperty(name = "category", value = "分类")
    private String category;

    /**
     * 关键字
     */
    @ApiModelProperty(name = "key", value = "关键字")
    private String key;

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "名称")
    private String name;

    /**
     * 是否部署
     */
    @ApiModelProperty(name = "deploymentFlag", value = "是否部署")
    private Boolean deploymentFlag = false;

    /**
     * 资源名称
     */
    @ApiModelProperty(name = "resourceName", value = "资源名称")
    private String resourceName;

    /**
     * 图标资源名称
     */
    @ApiModelProperty(name = "diagramResourceName", value = "图标资源名称")
    private String diagramResourceName;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    /**
     * 编辑器资源编号
     */
    @ApiModelProperty(name = "editorSourceValueId", value = "编辑器资源编号")
    private String editorSourceValueId;

    /**
     * 额外编辑器资源
     */
    @ApiModelProperty(name = "editorSourceExtValueId", value = "额外编辑器资源")
    private String editorSourceExtValueId;

    /**
     * 编辑器资源
     */
    @ApiModelProperty(name = "editorSource", value = "编辑器资源")
    private ObjectNode editorSource;

    /**
     * 额外编辑器资源
     */
    @ApiModelProperty(name = "editorSourceExt", value = "额外编辑器资源")
    private String editorSourceExt;
}
