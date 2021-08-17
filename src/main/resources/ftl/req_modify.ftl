package ${requestUrl};

import javax.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 修改${entityComment}
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Data
public class ${entityName}ModifyReq {

<#list cis as ci>
  <#if ci.property != "creator" && ci.property != "creatorName" && ci.property != "createTime"
  && ci.property != "updater"  && ci.property != "updaterName" && ci.property != "updateTime"
  && ci.property != "remark">
    /**
     * ${ci.comment}
     */
    <#if ci.property=="id">
    @NotNull(message = "id不能为空")
    </#if>
    <#if ci.javaType=="Date">
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}")
    private String ${ci.property};
    <#else>
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}")
    private ${ci.javaType} ${ci.property};
    </#if>

  </#if>
</#list>

}
	