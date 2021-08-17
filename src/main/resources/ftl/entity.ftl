package ${entityUrl};

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * ${entityComment}
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Data
public class ${entityName}{

<#list cis as ci>
    /**
     * ${ci.comment}
     */
    @ApiModelProperty(name = "${ci.property}", value = "${ci.comment}", position = ${(ci_index+1)*10})
    private ${ci.javaType} ${ci.property};

</#list>
<#if dictInfoList?size != 0>
    /**** Properties ****/

  <#list dictInfoList as dict>
    /**
     * ${dict.comment}
     */
    @ApiModelProperty(name = "${dict.property}Name", value = "${dict.comment}")
    private ${dict.javaType} ${dict.property}Name;

  </#list>
  <#list dictInfoList as dict>
    public String get${dict.enumName}Name() {
      if (StringUtils.isNotBlank(${dict.property})) {
        ${dict.property}Name = E${dict.entityName}${dict.enumName}.get${dict.entityName}${dict.enumName}(${dict.property}).getValue();
      }

      return ${dict.property}Name;
    }

  </#list>
</#if>


}
	