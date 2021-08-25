package com.newfiber.workflow.service.impl;

import com.newfiber.workflow.service.ActivitiCommonService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class ActivitiCommonServiceImpl implements ActivitiCommonService {

    @Resource
    private TaskService taskService;

    @Resource
    private IdentityService identityService;

    @Override
    public Set<String> listTaskUserId(Task task) {
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
        return listTaskUserId(new HashSet<>(identityLinkList));
    }

    @Override
    public Set<String> listTaskUserId(Set<IdentityLink> identityLinkSet) {
        Set<String> taskUserList = null;
        if(!CollectionUtils.isEmpty(identityLinkSet)){
            taskUserList = new HashSet<>();
            for(IdentityLink identityLink : identityLinkSet){
                if(StringUtils.isNotBlank(identityLink.getUserId())){
                    taskUserList.add(identityLink.getUserId());
                }
                if(StringUtils.isNotBlank(identityLink.getGroupId())){
                    List<User> userList = identityService.createUserQuery().memberOfGroup(identityLink.getGroupId()).list();
                    taskUserList.addAll(userList.stream().map(User::getId).collect(Collectors.toSet()));
                }
            }
        }
        return taskUserList;
    }
}
