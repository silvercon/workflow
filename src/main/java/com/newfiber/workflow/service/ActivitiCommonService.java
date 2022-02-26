package com.newfiber.workflow.service;

import java.util.Set;
import org.flowable.engine.task.IdentityLink;
import org.flowable.engine.task.Task;

public interface ActivitiCommonService {
    Set<String> listTaskUserId(Task task);
    Set<String> listTaskUserId(Set<IdentityLink> identityLinkSet);
}
