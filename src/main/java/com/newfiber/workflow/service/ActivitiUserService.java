package com.newfiber.workflow.service;

import com.newfiber.workflow.entity.WorkflowGroup;
import com.newfiber.workflow.entity.WorkflowUser;
import java.util.List;

/**
 * 工作流用户
 */
public interface ActivitiUserService {

    /**
     * 列表查询工作流用户
     * @return 工作流用户
     */
    List<WorkflowUser> listUser();

    /**
     * 列表查询工作流用户组
     * @return 工作流用户组
     */
    List<WorkflowGroup> listGroup();

}
