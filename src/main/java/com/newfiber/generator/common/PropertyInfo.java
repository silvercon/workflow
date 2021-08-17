package com.newfiber.generator.common;

import java.io.Serializable;
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
public class PropertyInfo implements Serializable {

    private static final long serialVersionUID = 123124L;

    private String column;

    private String jdbcType;

    private String comment;

    private String property;

    private String javaType;

    private String notNull;
}
