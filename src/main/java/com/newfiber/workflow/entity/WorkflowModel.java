package com.newfiber.workflow.entity;

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

}
