package com.newfiber.workflow.entity.support;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkflowCountersignUser {

    /**
     * 编号
     */
    @ApiModelProperty(name = "userId", value = "编号")
    private String userId;

    /**
     * 名称
     */
    @ApiModelProperty(name = "userName", value = "名称")
    private String userName;

    /**
     * 是否已会签（true/false）
     */
    @ApiModelProperty(name = "countersignFlag", value = "是否已会签（true/false）")
    private String countersignFlag;

}
