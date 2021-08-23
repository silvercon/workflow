package com.newfiber.config;

import com.newfiber.workflow.support.page.WorkflowPageInterceptor;
import com.newfiber.workflow.support.page.result.WorkflowPageResultInterceptor;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowInterceptorConfig {

    @Resource
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void addInterceptor() {
        WorkflowPageInterceptor workflowPageInterceptor = new WorkflowPageInterceptor();
        WorkflowPageResultInterceptor workflowPageResultInterceptor = new WorkflowPageResultInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if(!containsInterceptor(configuration, workflowPageInterceptor)){
                sqlSessionFactory.getConfiguration().addInterceptor(workflowPageInterceptor);
            }
            if(!containsInterceptor(configuration, workflowPageResultInterceptor)){
                sqlSessionFactory.getConfiguration().addInterceptor(workflowPageResultInterceptor);
            }
        }
    }

    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            return configuration.getInterceptors().contains(interceptor);
        } catch (Exception ignore) {
            return false;
        }
    }

}
