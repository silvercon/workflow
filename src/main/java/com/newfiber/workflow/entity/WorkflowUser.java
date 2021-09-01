package com.newfiber.workflow.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.activiti.engine.identity.User;

@Data
public class WorkflowUser implements User {

    /**
     * 编号
     */
    @ApiModelProperty(name = "id", value = "编号")
    private String id;

    /**
     * 姓
     */
    @ApiModelProperty(name = "firstName", value = "姓")
    private String firstName;

    /**
     * 名
     */
    @ApiModelProperty(name = "lastName", value = "名")
    private String lastName;

    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email", value = "邮箱")
    private String email;

    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码")
    private String password;

    @Override
    public boolean isPictureSet() {
        return false;
    }

    public static List<WorkflowUser> build(List<User> userList){
        List<WorkflowUser> workflowUserList = new ArrayList<>();
        userList.forEach(t -> {
            WorkflowUser workflowUser = new WorkflowUser();
            workflowUser.setId(t.getId());
            workflowUser.setFirstName(t.getFirstName());
            workflowUser.setLastName(t.getLastName());
            workflowUser.setEmail(t.getEmail());
            workflowUser.setPassword(t.getPassword());
            workflowUserList.add(workflowUser);
        });
        return workflowUserList;
    }
}
