package com.newfiber.workflow.service.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.newfiber.workflow.service.ActivitiEditorService;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivitiEditorServiceImpl implements ActivitiEditorService {

    @Override
    public Object stencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json.zh-cn");
        try {
            if(null != stencilsetStream){
                String result = IoUtil.readUtf8(stencilsetStream);
                return JSONObject.parse(result);
            }else{
                return "";
            }
        } catch (Exception e) {
            log.error("加载BPMN按钮失败：{}-->{}", e.getMessage(), e.getStackTrace());
            throw new ActivitiException(String.format("加载BPMN按钮失败【%s】", e.getMessage()));
        }
    }
}
