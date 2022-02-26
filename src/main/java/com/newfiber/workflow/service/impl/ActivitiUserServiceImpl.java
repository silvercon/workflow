package com.newfiber.workflow.service.impl;

import com.newfiber.workflow.entity.WorkflowGroup;
import com.newfiber.workflow.entity.WorkflowUser;
import com.newfiber.workflow.service.ActivitiUserService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivitiUserServiceImpl implements ActivitiUserService {

    @Resource
    private IdentityService identityService;

    @Override
    public List<WorkflowUser> listUser() {
        List<User> userList = identityService.createUserQuery().list();
        return WorkflowUser.build(userList);
    }

    @Override
    public List<WorkflowGroup> listGroup() {
        List<Group> groupList = identityService.createGroupQuery().list();
        return WorkflowGroup.build(groupList);
    }
}
