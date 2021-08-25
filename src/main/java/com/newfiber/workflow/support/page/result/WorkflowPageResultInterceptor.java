package com.newfiber.workflow.support.page.result;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.db.handler.BeanHandler;
import com.newfiber.workflow.service.ActivitiProcessService;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Slf4j
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})}
)
public class WorkflowPageResultInterceptor implements Interceptor{

    @Resource
    private ActivitiProcessService activitiProcessService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(defaultResultSetHandler, "mappedStatement");
        Class<?> classType = mappedStatement.getResultMaps().get(0).getType();

        if(WorkflowPageResult.class.isAssignableFrom(classType)){
            Statement statement = (Statement) invocation.getArgs()[0];
            ResultSet resultSet = statement.getResultSet();
            List<WorkflowPageResult> resultList = new ArrayList<>();

            BeanHandler<?> beanHandler = BeanHandler.create(classType);
            WorkflowPageResult workflowPageResult = (WorkflowPageResult) beanHandler.handle(resultSet);
            while(null != workflowPageResult){
                // 会签的执行人
                workflowPageResult.setCountersignUserList(null);

                resultList.add(workflowPageResult);
                workflowPageResult = (WorkflowPageResult) beanHandler.handle(resultSet);
            }
            return resultList;
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
