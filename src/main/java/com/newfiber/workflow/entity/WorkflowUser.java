package com.newfiber.workflow.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.flowable.idm.api.User;

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

	/**
	 * 显示名称
	 */
	@ApiModelProperty(name = "displayName", value = "显示名称")
    private String displayName;

	/**
	 */
	@ApiModelProperty(name = "tenantId", hidden = true)
    private String tenantId;

	/**
	 */
	@ApiModelProperty(name = "pictureSet", hidden = true)
    private boolean pictureSet;

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
