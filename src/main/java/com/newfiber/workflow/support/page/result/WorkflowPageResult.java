package com.newfiber.workflow.support.page.result;

import com.newfiber.workflow.entity.support.WorkflowCountersignUser;
import java.util.List;

public interface WorkflowPageResult {

    /**
     * 工作流会签用户
     * @param countersignUserList 工作流会签用户
     */
    void setCountersignUserList(List<WorkflowCountersignUser> countersignUserList);

}
