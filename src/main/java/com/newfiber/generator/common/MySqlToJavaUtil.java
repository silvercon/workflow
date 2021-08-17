package com.newfiber.generator.common;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:17
 */
public class MySqlToJavaUtil {

    public static String getClassName(String table) {

//        table = changeToJavaFiled(table);
        table = changeToJavaFiled(table.replace(table.split("_")[0] + "_", ""));
        StringBuilder sbuilder = new StringBuilder();
        char[] cs = table.toCharArray();
        cs[0] -= 32;
        sbuilder.append(String.valueOf(cs));
        return sbuilder.toString();
    }

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param field 驼峰字段
     * @return 下划线字段
     */
    public static String changeToJavaFiled(String field) {
        String[] fields = field.split("_");
        StringBuilder sbuilder = new StringBuilder(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            char[] cs = fields[i].toCharArray();
            cs[0] -= 32;
            sbuilder.append(String.valueOf(cs));
        }
        return sbuilder.toString();
    }
    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param field 下划线字段
     * @return 驼峰字段
     */
    public static String changeToDbFiled(String field) {
        if (field == null || "".equals(field.trim())) {
            return "";
        }
        int len = field.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = field.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String formatJdbcType(String sqlType) {

        switch (sqlType.toLowerCase()) {
            case "int4":
                return "integer";

            case "int8":
                return "bigint";

            case "float8":
                return "double";

            case "text":
                return"varchar";

            case "_int8":
                return"array";

            default:
                return sqlType;
        }

    }

    public static String jdbcTypeToJavaType(String sqlType) {

        switch (sqlType.toLowerCase()) {

            case "bit":
                return "Boolean";

            case "int":
            case "tinyint":
            case "smallint":
            case "integer":
                return "Integer";

            case "bigint":
                return "Long";

            case "float":
                return "Float";

            case "decimal":
            case "numeric":
            case "real":
            case "money":
            case "smallmoney":
            case "double":
                return "Double";

            case "varchar":
            case "char":
            case "nvarchar":
            case "nchar":
            case "text":
                return "String";

            case "datetime":
            case "date":
            case "timestamp":
                return "Date";

            case "image":
                return "Blod";

            case "array":
                return "Long[]";

            default:
                return "未知数据库类型，请手动添加后生成";

        }

//        if (sqlType.equalsIgnoreCase("bit")) {
//
//        } else if (sqlType.equalsIgnoreCase("tinyint")) {
//
//        } else if (sqlType.equalsIgnoreCase("smallint")) {
//            return "Integer";
//        } else if (sqlType.equalsIgnoreCase("int")) {
//            return "Integer";
//        } else if (sqlType.equalsIgnoreCase("bigint")) {
//            return "Long";
//        } else if (sqlType.equalsIgnoreCase("float")) {
//            return "Float";
//        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
//                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") || sqlType
//                .equalsIgnoreCase("smallmoney")) {
//            return "Double";
//        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
//                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
//                || sqlType.equalsIgnoreCase("text") || sqlType
//                .equalsIgnoreCase("character varying(8)")) {
//            return "String";
//        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")
//                || sqlType.equalsIgnoreCase("timestamp")) {
//            return "Date";
//        } else if (sqlType.equalsIgnoreCase("image")) {
//            return "Blod";
//        }
//        return null;
    }
}
