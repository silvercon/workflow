package com.newfiber.core.result;

/**
 * @author : xieyj
 * @since : 2019-01-18 13:48
 */
public class ResultCode {

    /**
     * 常用应答码定义：业务处理成功
     */
    public static final String SUCCESS = "200";

    /**
     * 常用应答码定义：权限异常
     */
    public static final String AUTH_FORBIDDEN = "300";

    /**
     * 常用应答码定义：参数校验异常
     */
    public static final String PARA_ERROR = "400";

    /**
     * 常用应答码定义：业务处理失败
     */
    public static final String BIZ_EXCEPTION = "500";

    /**
     * 常用应答码定义：其他未知异常
     */
    public static final String ERROR = "600";
}
