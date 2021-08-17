package com.newfiber.workflow.entity.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowModelMetaInfo {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

}
