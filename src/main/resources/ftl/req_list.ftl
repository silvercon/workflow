package ${requestUrl};

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 列表查询${entityComment}
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Data
public class ${entityName}ListReq {

<#list cis as ci>
    /**
     * ${ci.comment}
     */
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}")
    private ${ci.javaType} ${ci.property};

</#list>

}
	