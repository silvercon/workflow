package com.newfiber.workflow.service;

import com.newfiber.workflow.entity.WorkflowModel;
import com.newfiber.workflow.entity.request.WorkflowModeCreateRequest;
import com.newfiber.workflow.entity.request.WorkflowModelPageRequest;
import com.newfiber.workflow.entity.response.WorkflowModelNextTaskResponse;
import com.newfiber.workflow.utils.PageWrapper;
import java.util.List;
import org.flowable.engine.repository.Model;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工作流模型
 */
public interface ActivitiModelService {

    /**
     * 新增
     * @param request 修改参数
     */
    Model create(WorkflowModeCreateRequest request);

    /**
     * 保存
     */
    void save(String modelId, String key, String name, String jsonXml, String svgXml, String description);

    /**
     * 部署
     * @param modelId 模型编号
     */
    void deploy(String modelId);

    /**
     * 删除
     * @param modelId 模型编号
     */
    void delete(String modelId);

    /**
     * 上传模型文件
     * @param workflowKey 工作流编号
     * @param multipartFile 模型文件
     */
    void upload(String workflowKey, MultipartFile multipartFile);

	/**
	 * 上传模型文件，删除旧模型
	 */
	void reUpload(String workflowKey, MultipartFile multipartFile);

	/**
	 * 查询任务的下一步任务
	 *
	 * @param workflowKey 工作流编号
	 * @param currentTask 当前任务
	 */
	List<WorkflowModelNextTaskResponse> nextTasks(String workflowKey, String currentTask);

    /**
     * 详细查
     * @param modelId 模型编号
     * @return 模型
     */
    WorkflowModel detail(String modelId);

    /**
     * 分页查询工作流模型
     * @param request 分页参数
     * @return 工作流模型
     */
    PageWrapper<WorkflowModel> pageWorkflowModel(WorkflowModelPageRequest request);

}
