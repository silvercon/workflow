package com.newfiber.generator;

import com.alibaba.fastjson.JSON;
import com.newfiber.generator.common.BasisInfo;
import com.newfiber.generator.common.Dict;
import com.newfiber.generator.common.DictInfo;
import com.newfiber.generator.common.MySqlToJavaUtil;
import com.newfiber.generator.common.PropertyInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:17
 */
public class EntityInfoUtil {

    public static BasisInfo getInfo(BasisInfo bi) throws SQLException {

        List<PropertyInfo> columns = new ArrayList<PropertyInfo>();

        List<DictInfo> dictInfoList = new ArrayList<DictInfo>();

        // 创建连接
        //sql
        String sql =
            "select column_comment, data_type, column_name, is_nullable from information_schema.columns where table_schema='"
                + bi.getDatabase() + "' and table_name='" + bi.getTable() + "'";

//        String sql =
//                "SELECT col_description(a.attrelid,a.attnum) as comment,format_type(a.atttypid,a.atttypmod) as type,a.attname as name, a.attnotnull as notnull   \n"
//                        + "FROM pg_class as c,pg_attribute as a where c.relname = '" + bi.getTable()
//                        + "' and a.attrelid = c.oid and a.attnum>0";

        // postgreSql 注释，字段类型，字段名，是否为空 查询SQL语句
//        String sql =
//                "SELECT col_description(attrelid,attnum),typname,attname,a.attnotnull as notnull FROM pg_class c,pg_attribute a,pg_type t WHERE c.relname = '"
//                        + bi.getTable()
//                        + "' AND c.oid = attrelid AND atttypid = t.oid AND attnum > 0";

        try (Connection con = DriverManager.getConnection(bi.getDbUrl(), bi.getDbName(),
                bi.getDbPassword()); PreparedStatement pstemt = con.prepareStatement(sql)) {

            ResultSet executeQuery = pstemt.executeQuery();
            while (executeQuery.next()) {
                String comment = executeQuery.getString(1);
                String jdbcType = executeQuery.getString(2);
                String column = executeQuery.getString(3);
                String notNull = executeQuery.getString(4);

                PropertyInfo ci = new PropertyInfo();
                ci.setColumn(column);
                ci.setNotNull(notNull);
                ci.setComment(comment.replace(bi.getDictSign(), ""));
                ci.setProperty(MySqlToJavaUtil.changeToJavaFiled(column));
                ci.setJdbcType(MySqlToJavaUtil.formatJdbcType(jdbcType));
                ci.setJavaType(MySqlToJavaUtil.jdbcTypeToJavaType(ci.getJdbcType()));

                //设置注解类型
                if ("id".equalsIgnoreCase(column)) {
                    bi.setIdType(ci.getJavaType());
                    bi.setIdJdbcType(ci.getJdbcType());
                }
                columns.add(ci);

                // 解析数据字典
                String[] comments = comment.split(bi.getDictSign());
                if (comments.length == 2) {

                    Map map = JSON.parseObject(comments[1].trim(), Map.class);

                    DictInfo dictInfo = new DictInfo();
                    dictInfo.setEnumUrl(bi.getEnumUrl());
                    dictInfo.setAuthor(bi.getAuthor());
                    dictInfo.setCreateTime(bi.getCreateTime());

                    dictInfo.setEntityName(bi.getEntityName());
                    dictInfo.setEntityComment(bi.getEntityComment());
                    dictInfo.setUnderLineName(bi.getRequestMappingUrl());
                    dictInfo.setProperty(ci.getProperty());
                    dictInfo.setJavaType(ci.getJavaType());

                    dictInfo.setComment(ci.getComment());
                    dictInfo.setEnumName(toUpperCaseFirstOne(ci.getProperty()));
                    dictInfo.setEnumContent(comments[1].trim());
                    dictInfo.setEnumRemark(bi.getEntityComment() + comments[0].trim());

                    List<Dict> dictList = new ArrayList<>();
                    for (Object key : map.keySet()) {
                        dictList.add(
                                new Dict().setKey((String) key).setValue((String) map.get(key)));
                    }
                    dictInfo.setDictList(dictList);

                    dictInfoList.add(dictInfo);
                }

            }

            if (columns.size() == 0) {
                throw new Exception("未找到表，或表内字段数量为0，请检查后重试");
            }

            bi.setCis(columns);
            bi.setDictInfoList(dictInfoList);
            return bi;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("自动生成实体类错误：" + e.getMessage());
        }
    }


    public static String getGeneratorFileUrl(String url, String packageUrl, String entityName,
            String type) {
        if (Generator.ENTITY.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + ".java";
        } else if (Generator.MAPPER.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "Mapper.java";
        } else if (Generator.MAPPER_XML.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "Mapper.xml";
        } else if (Generator.SERVICE.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "Service.java";
        } else if (Generator.SERVICE_IMPL.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "ServiceImpl.java";
        } else if (Generator.CONTROLLER.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "Controller.java";
        } else if (Generator.REQUEST_CREATE.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "CreateReq.java";
        } else if (Generator.REQUEST_MODIFY.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "ModifyReq.java";
        } else if (Generator.REQUEST_PAGE.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "PageReq.java";
        } else if (Generator.REQUEST_LIST.equals(type)) {
            return url + pageToUrl(packageUrl) + entityName + "ListReq.java";
        } else if (Generator.ENUM.equals(type)) {
            // 此处 entityName = entityName + dictName
            return url + pageToUrl(packageUrl) + "E" + entityName + ".java";
        }
        return null;
    }

    public static String pageToUrl(String url) {
        return url.replace(".", "/") + "/";
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("ada -!{}".replace("-!", ""));
        System.out.println("ada {}".replace("-!", ""));
    }
}


