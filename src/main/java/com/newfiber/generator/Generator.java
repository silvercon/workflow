package com.newfiber.generator;

import com.newfiber.generator.common.BasisInfo;
import com.newfiber.generator.common.DictInfo;
import com.newfiber.generator.common.JsonResult;
import com.newfiber.generator.common.PropertyInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:17
 */
public class Generator {

    //路径信息
    public static final String ENTITY = "entity";
    public static final String MAPPER = "mapper";
    public static final String MAPPER_XML = "mapperXml";
    public static final String SERVICE = "service";
    public static final String SERVICE_IMPL = "serviceImpl";
    public static final String CONTROLLER = "controller";
    public static final String REQUEST_CREATE = "request_create";
    public static final String REQUEST_MODIFY = "request_modify";
    public static final String REQUEST_PAGE = "request_page";
    public static final String REQUEST_LIST = "request_list";
    public static final String ENUM = "enum";


    //①创建实体类
    public static JsonResult createEntity(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getEntityUrl(), bi.getEntityName(), ENTITY);
        return FreemarkerUtil.createFile(bi, "entity.ftl", fileUrl);
    }

    //②创建mapper
    public static JsonResult createMapper(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getMapperUrl(), bi.getEntityName(), MAPPER);
        return FreemarkerUtil.createFile(bi, "mapper.ftl", fileUrl);
    }

    //③创建mapper配置文件
    public static JsonResult createMapperImpl(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getMapperXmlUrl(), bi.getEntityName(), MAPPER_XML);
        List<PropertyInfo> list = bi.getCis();
        String agile = "";
        for (PropertyInfo propertyInfo : list) {
            agile = agile + propertyInfo.getColumn() + ", ";
        }
        agile = agile.substring(0, agile.length() - 2);
        bi.setAgile(agile);
        return FreemarkerUtil.createFile(bi, "mapper_xml.ftl", fileUrl);
    }

    //④创建SERVICE
    public static JsonResult createService(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getServiceUrl(), bi.getEntityName(), SERVICE);
        return FreemarkerUtil.createFile(bi, "service.ftl", fileUrl);
    }

    //⑤创建SERVICE_IMPL
    public static JsonResult createServiceImpl(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getServiceImplUrl(), bi.getEntityName(), SERVICE_IMPL);
        return FreemarkerUtil.createFile(bi, "serviceImpl.ftl", fileUrl);
    }

    //⑥创建CONTROLLER
    public static JsonResult createController(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getControllerUrl(), bi.getEntityName(), CONTROLLER);
        return FreemarkerUtil.createFile(bi, "controller.ftl", fileUrl);
    }

    //创建CREATE_REQ
    public static JsonResult createCreateReq(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getRequestUrl(), bi.getEntityName(), REQUEST_CREATE);
        return FreemarkerUtil.createFile(bi, "req_create.ftl", fileUrl);
    }

    //创建CREATE_MODIFY
    public static JsonResult createModifyReq(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getRequestUrl(), bi.getEntityName(), REQUEST_MODIFY);
        return FreemarkerUtil.createFile(bi, "req_modify.ftl", fileUrl);
    }

    //创建CREATE_PAGE
    public static JsonResult createPageReq(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getRequestUrl(), bi.getEntityName(), REQUEST_PAGE);
        return FreemarkerUtil.createFile(bi, "req_page.ftl", fileUrl);
    }

    /**
     * 创建ListReq
     *
     * @param url 文件路径
     * @param bi 字段信息
     * @return log
     */
    static JsonResult createListReq(String url, BasisInfo bi) {
        String fileUrl = EntityInfoUtil
                .getGeneratorFileUrl(url, bi.getRequestUrl(), bi.getEntityName(), REQUEST_LIST);
        return FreemarkerUtil.createFile(bi, "req_list.ftl", fileUrl);
    }

    /**
     * 创建Enum
     */
    static List<JsonResult> createEnum(String url, BasisInfo bi) {

        List<JsonResult> jsonResultList = new ArrayList<>();

        for (DictInfo dictInfo : bi.getDictInfoList()) {

            String fileUrl = EntityInfoUtil
                    .getGeneratorFileUrl(url, bi.getEnumUrl(),
                            dictInfo.getEntityName() + dictInfo.getEnumName(), ENUM);
            jsonResultList.add(FreemarkerUtil.createFile(dictInfo, "enum.ftl", fileUrl));

        }

        return jsonResultList;
    }
}
