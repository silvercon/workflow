<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperUrl}.${entityName}Dao">
    <resultMap id="BaseResultMap" type="${entityUrl}.${entityName}">
      <#list cis as ci>
        <#if ci_index == 0>
        <id column="${ci.column}" jdbcType="${ci.jdbcType?upper_case}" property="${ci.property}"/>
        <#else>
        <result column="${ci.column}" jdbcType="${ci.jdbcType?upper_case}" property="${ci.property}"/>
        </#if>
      </#list>
    </resultMap>

    <sql id="Base_Column_List">
      <#list cis as ci>
        <#if ci_index == 0>
        t.${ci.column}
        <#else>
        , t.${ci.column}
        </#if>
      </#list>
    </sql>

    <sql id="where_condition">
        <trim prefix="WHERE" prefixOverrides="AND | OR">
        <#list cis as ci>
          <#if ci.column == "name">
            <if test="name != null and name != '' ">
                AND t.name like concat('%',${r'#{name, jdbcType=VARCHAR}'},'%')
            </if>
            <#else>
            <#if ci.jdbcType == "varchar" || ci.jdbcType == "text">
            <if test="${ci.property} != null and ${ci.property} != '' ">
            <#else>
            <if test="${ci.property} != null">
              </#if>
                AND t.${ci.column} = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
            </if>
          </#if>
        </#list>
        </trim>
    </sql>

    <!-- 组合条件查询 -->
    <select id="selectByCondition" parameterType="${entityUrl}.${entityName}"
            resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${table} t
        <include refid="where_condition"/>
    </select>
</mapper>
