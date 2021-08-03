package com.newfiber.workflow.service;

import com.newfiber.workflow.entity.WorkflowModel;
import com.newfiber.workflow.entity.request.WorkflowModelPageRequest;
import com.newfiber.workflow.entity.response.WorkflowModelNextTaskResponse;
import com.newfiber.workflow.utils.PageWrapper;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ActivitiService {

    void delete(String deploymentId);

    void upload(MultipartFile multipartFile);

    List<WorkflowModelNextTaskResponse> nextTasks(String workflowModelKey, String currentTask);

    PageWrapper<WorkflowModel> pageWorkflowModel(WorkflowModelPageRequest request);

}
