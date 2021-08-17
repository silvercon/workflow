package com.newfiber.generator.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:23
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 123126L;

    private Integer code;

    private String message;

    private Object data;
}
