package ${requestUrl};

import ${commonUrl}.base.BasePageReq;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页查询${entityComment}
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Data
public class ${entityName}PageReq extends BasePageReq{

<#list cis as ci>
    /**
     * ${ci.comment}
     */
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}")
    private ${ci.javaType} ${ci.property};

</#list>

}