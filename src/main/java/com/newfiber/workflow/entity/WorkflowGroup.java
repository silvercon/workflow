package com.newfiber.workflow.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.flowable.idm.api.Group;

@Data
public class WorkflowGroup implements Group {

    /**
     * 编号
     */
    @ApiModelProperty(name = "id", value = "编号")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "名称")
    private String name;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型")
    private String type;

    public static List<WorkflowGroup> build(List<Group> groupList){
        List<WorkflowGroup> workflowGroupList = new ArrayList<>();
        groupList.forEach(t -> {
            WorkflowGroup workflowGroup = new WorkflowGroup();
            workflowGroup.setId(t.getId());
            workflowGroup.setName(t.getName());
            workflowGroup.setType(t.getType());
            workflowGroupList.add(workflowGroup);
        });
        return workflowGroupList;
    }
}
