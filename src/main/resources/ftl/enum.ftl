package ${enumUrl};

import com.ais.common.exception.BizException;
import java.util.HashMap;
import java.util.Map;

/**
* ${entityComment}Enum
*
* @author : ${author}
* @since : ${createTime}
*/
public enum E${entityName}${enumName} {

// 数据字典执行SQL
// INSERT INTO "public"."tsys_dict"(key, value, remark) VALUES ('${underLineName?lower_case}.${enumName?lower_case}', '${enumContent}', '${enumRemark}');

<#list dictList as dict>
    /**
    * ${dict.value}
    */
    ${underLineName?upper_case}_${enumName?upper_case}_${dict_index}("${dict.key}", "${dict.value}"),

</#list>
;

private String code;
private String value;

E${entityName}${enumName}(String code, String value) {
this.code = code;
this.value = value;
}

public String getCode() {
return code;
}

public String getValue() {
return value;
}

public static Map
<String, E${entityName}${enumName}> get${entityName}${enumName}ResultMap() {
Map
<String, E${entityName}${enumName}> map = new HashMap
<String, E${entityName}${enumName}>();
for (E${entityName}${enumName} type : E${entityName}${enumName}.values()) {
map.put(type.getCode(), type);
}

return map;
}

public static E${entityName}${enumName} get${entityName}${enumName}(String code) {
Map
<String, E${entityName}${enumName}> map = get${entityName}${enumName}ResultMap();
E${entityName}${enumName} result = map.get(code);
if (result == null) {
throw new ActivitiException(ECommonErrorCode.E500001.getCode(),
ECommonErrorCode.E500001.getValue(), "E${entityName}${enumName}=" + code);
}

return result;
}

}
	