package com.newfiber.core.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : xieyj
 * @since : 2019-01-18 14:10
 */
@Data
public class BaseListReq {

    /**
     * 排序字段
     */
    @ApiModelProperty(name = "orderBy", value = "排序字段")
    private String orderBy;

}
