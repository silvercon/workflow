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
public class DictInfo implements Serializable {

    private String enumUrl;

    private String author;

    private String createTime;

    private String entityName;

    private String underLineName;

    private String entityComment;

    private String property;

    private String javaType;

    private String comment;

    private String enumName;

    private String enumContent;

    private String enumRemark;

    private List<Dict> dictList;
}
