package com.newfiber.workflow.service;

import com.newfiber.workflow.entity.WorkflowGroup;
import com.newfiber.workflow.entity.WorkflowUser;
import java.util.List;

public interface ActivitiUserService {

    List<WorkflowUser> listUser();

    List<WorkflowGroup> listGroup();

}
