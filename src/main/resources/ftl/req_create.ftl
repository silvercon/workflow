package ${requestUrl};

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增${entityComment}
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Data
public class ${entityName}CreateReq {

<#list cis as ci>
  <#if ci.property != "id" && ci.property != "creator" && ci.property != "creatorName"
  && ci.property != "createTime"  && ci.property != "updater"  && ci.property != "updaterName"
  && ci.property != "updateTime" && ci.property != "remark">
    /**
     * ${ci.comment}
     */
    <#if ci.notNull=="t">
      <#if ci.javaType=="String">
    @NotBlank(message = "不能为空")
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}", required = true)
    private ${ci.javaType} ${ci.property};
      <#elseif ci.javaType=="Date">
    @NotBlank(message = "不能为空")
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}", required = true)
    private String ${ci.property};
      <#else>
    @NotNull(message = "不能为空")
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}", required = true)
    private ${ci.javaType} ${ci.property};
      </#if>
    <#else>
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}")
    private ${ci.javaType} ${ci.property};
    </#if>

  </#if>
</#list>

}
	