package com.newfiber.generator;

import com.newfiber.generator.common.BasisInfo;
import com.newfiber.generator.common.MySqlToJavaUtil;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:17
 */
public class GeneratorV2Util {

    // 基础
    private static final String PROJECT = "huludao-project";
    private static final String AUTHOR = "xiongk";

    // 数据库
    private static final String URL = "jdbc:mysql://119.27.160.75:62674/nf_monitor";
    private static final String NAME = "root";
    private static final String PASSWORD = "xfgdgfA20202328";
    private static final String DATABASE = "nf_monitor";
    private static final String AGILE = System.currentTimeMillis() + "";

    // 数据字典转译起止符
    private static final String DICT_SIGN = "dict=";

    // 表
    private static final String TABLE = "df_monitor_copy_record";
    private static final String TABLE_COMMENT = "监测数据复制记录（数据补录的一种方式）";

    // 支持规则以外需要手动填写
    private static final String ENTITY_NAME = null;
    private static final String OBJECT_NAME = null;
    private static final String REQUEST_MAPPING_URL = null;

    // 主键类型
    private static final String ID_TYPE = "Integer";
    private static final String ID_JDBC_TYPE = "INTEGER";

    // 路径
    private static final String PACKAGE = "com.xf.huludao";
    private static final String COMMON_URL = "com.xf.huludao.commons";
    private static final String ENTITY_URL = PACKAGE + ".entity";
    private static final String REQUEST_URL = PACKAGE + ".entity.request";
    private static final String MAPPER_URL = PACKAGE + ".dao";
    private static final String MAPPER_XML_URL = "mapper";
    private static final String SERVICE_URL = PACKAGE + ".service";
    private static final String SERVICE_IMPL_URL = PACKAGE + ".service.impl";
    private static final String CONTROLLER_URL = PACKAGE + ".controller";
    private static final String ENUM_URL = PACKAGE + ".enums";

  private static final String WORKSPACE = "C:\\Users\\Administrator\\Desktop\\";

    /**
     * 建表工具使用注意事项:
     * 1.所有生成文件务必自行代码格式化
     * 2.务必检查各Req入参及其必填注解
     * 3.检查mapper.xml文件中“where_condition”是否符合条件查询入参
     * 4.如果要生成枚举请在数据库字段注释后添加 确认生成起始符"dict=" 和 供解析的JSON数据且保证JSON数据格式的正确性 添加后的注释
     * 例如: 状态 dict={"1":"创建","2":"删除"}
     * 5.已生成的枚举类需要自行修改各枚举名称，不推荐使用默认名称
     * 6.请自行执行 枚举类中的数据字典SQL语句，新增数据字典
     */
    //
    public static void main(String[] args) {
        BasisInfo bi = new BasisInfo(PROJECT,
            AUTHOR,

            URL,
            NAME,
            PASSWORD,
            DATABASE,
            AGILE,

            TABLE,
            TABLE_COMMENT,

            ENTITY_NAME,
            OBJECT_NAME,
            REQUEST_MAPPING_URL,

            ID_TYPE,
            ID_JDBC_TYPE,

            COMMON_URL,
            ENTITY_URL,
            REQUEST_URL,
            MAPPER_URL,
            MAPPER_XML_URL,
            SERVICE_URL,
            SERVICE_IMPL_URL,
            CONTROLLER_URL,
            ENUM_URL);

        // 设置
        bi.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        bi.setDictSign(DICT_SIGN);

        // 当此三字段为空时使用默认规则添加
        if (null == bi.getEntityName()) {
            bi.setEntityName(MySqlToJavaUtil.getClassName(TABLE));
        }
        if (null == bi.getObjectName()) {
            bi.setObjectName(EntityInfoUtil.toLowerCaseFirstOne(bi.getEntityName()));
        }
        if (null == bi.getRequestMappingUrl()) {
            bi.setRequestMappingUrl(MySqlToJavaUtil.changeToDbFiled(bi.getObjectName()));
        }

        try {
            bi = EntityInfoUtil.getInfo(bi);
            String entity = Generator
                    .createEntity(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(entity);
////
            String mapper = Generator
                    .createMapper(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(mapper);

            String mapperImpl = Generator
                    .createMapperImpl(WORKSPACE + "src/main/resources/", bi)
                .toString();
            System.out.println(mapperImpl);

            String service = Generator
                    .createService(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(service);

            String serviceImpl = Generator
                    .createServiceImpl(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(serviceImpl);
//
            String controller = Generator
                    .createController(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(controller);
//
            String createReq = Generator
                    .createCreateReq(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(createReq);

            String modifyReq = Generator
                    .createModifyReq(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(modifyReq);

            String pageReq = Generator
                    .createPageReq(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(pageReq);

            String listReq = Generator
                    .createListReq(WORKSPACE + "src/main/java/", bi)
                .toString();
            System.out.println(listReq);
//
//            List<JsonResult> enums = Generator.createEnum(WORKSPACE + "src/main/java/", bi);
//            for (JsonResult jsonResult : enums) {
//                System.out.println(jsonResult.toString());
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
