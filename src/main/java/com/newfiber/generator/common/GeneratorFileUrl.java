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
public class GeneratorFileUrl implements Serializable {

    private static final long serialVersionUID = 123125L;

    private String entityUrl;

    private String daoUrl;

    private String mapperUrl;

    private String serviceUrl;

    private String serviceImplUrl;

    private String controllerUrl;
}
