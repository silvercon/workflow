package com.newfiber.workflow.service;

import java.util.Set;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

public interface ActivitiCommonService {
    Set<String> listTaskUserId(Task task);
    Set<String> listTaskUserId(Set<IdentityLink> identityLinkSet);
}
