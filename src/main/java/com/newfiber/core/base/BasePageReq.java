package com.newfiber.core.base;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : xieyj
 * @since : 2019-01-18 14:10
 */
@Data
public class BasePageReq {

    /**
     * 起始页
     */
    @ApiModelProperty(name = "pageNum", value = "起始页")
    @NotNull(message = "起始页不能为空")
    @Min(value = 0, message = "起始页要大于0")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(name = "pageSize", value = "每页数量")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 0, message = "每页数量要大于0")
    private Integer pageSize;

    /**
     * 排序字段
     */
    @ApiModelProperty(name = "orderBy", value = "排序字段")
    private String orderBy;

}
