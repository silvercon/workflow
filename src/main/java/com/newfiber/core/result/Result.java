package com.newfiber.core.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : xieyj
 * @since : 2019-01-18 13:48
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Result<T> {

    /**
     * 错误类别码
     */
    @ApiModelProperty(name = "code", value = "错误类别码")
    private String code;

    /**
     * 错误码说明
     */
    @ApiModelProperty(name = "errorMsg", value = "错误码说明")
    private String message;

    /**
     * 业务数据
     */
    @ApiModelProperty(name = "data", value = "业务数据")
    private T data;

    public Result() {
        this(ResultCode.SUCCESS, "SUCCESS", null);
    }

    public Result(T data) {
        this(ResultCode.SUCCESS, "SUCCESS", data);
    }

    public Result(String code, T object) {
        this.code = code;
        this.data = object;
    }

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
