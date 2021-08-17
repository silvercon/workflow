package com.newfiber.generator.common;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasisInfo implements Serializable {

    private static final long serialVersionUID = 123123L;

    private String project;

    private String author;

    private String dbUrl;

    private String dbName;

    private String dbPassword;

    private String database;

    private String table;

    private String requestMappingUrl;

    private String entityName;

    private String objectName;

    private String entityComment;

    private String createTime;

    private String agile;

    private String commonUrl;

    private String entityUrl;

    private String requestUrl;

    private String mapperUrl;

    private String mapperXmlUrl;

    private String serviceUrl;

    private String serviceImplUrl;

    private String controllerUrl;

    private String enumUrl;

    private String idType;

    private String idJdbcType;

    private List<PropertyInfo> cis;

    private List<DictInfo> dictInfoList;

    private String dictSign;

    public BasisInfo(String project,
            String author,

            String dbUrl,
            String dbName,
            String dbPassword,
            String database,
            String agile,

            String table,
            String entityComment,

            String entityName,
            String objectName,
            String requestMappingUrl,

            String idType,
            String idJdbcType,

            String commonUrl,
            String entityUrl,
            String requestUrl,
            String mapperUrl,
            String mapperXmlUrl,
            String serviceUrl,
            String serviceImplUrl,
            String controllerUrl,
            String enumUrl) {

        super();
        this.project = project;
        this.author = author;

        this.dbUrl = dbUrl;
        this.dbName = dbName;
        this.dbPassword = dbPassword;
        this.database = database;
        this.agile = agile;

        this.table = table;
        this.entityComment = entityComment;

        this.entityName = entityName;
        this.objectName = objectName;
        this.requestMappingUrl = requestMappingUrl;

        this.idType = idType;
        this.idJdbcType = idJdbcType;

        this.commonUrl = commonUrl;
        this.entityUrl = entityUrl;
        this.requestUrl = requestUrl;
        this.mapperUrl = mapperUrl;
        this.mapperXmlUrl = mapperXmlUrl;
        this.serviceUrl = serviceUrl;
        this.serviceImplUrl = serviceImplUrl;
        this.controllerUrl = controllerUrl;
        this.enumUrl = enumUrl;
    }
}
