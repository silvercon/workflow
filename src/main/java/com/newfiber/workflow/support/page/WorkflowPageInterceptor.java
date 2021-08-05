package com.newfiber.workflow.support.page;

import com.newfiber.workflow.service.ActivitiProcessService;
import com.newfiber.workflow.utils.ApplicationContextProvider;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;

@Slf4j
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        }
)
public class WorkflowPageInterceptor implements Interceptor{

    private ActivitiProcessService activitiProcessService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        WorkflowPage workflowPage = WorkflowPageHelper.getWorkflowPage();
        if(null == workflowPage){
            return invocation.proceed();
        }

        resourceCheck();

        List<String> businessKeyList = activitiProcessService.listTodoBusinessKeyByUser(
                workflowPage.getWorkflowCallback().getWorkflowDefinition().getWorkflowKey(),
                workflowPage.getTaskKey(), workflowPage.getUserId());

        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);

        String executeSql = boundSql.getSql();
        executeSql = appendWhereIdCondition(executeSql, businessKeyList);

        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), executeSql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());

        MappedStatement newMappedStatement = buildMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        invocation.getArgs()[0] = newMappedStatement;

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String appendWhereIdCondition(String sql, List<String> idList){
        if(CollectionUtils.isEmpty(idList)){
            idList.add("0");
        }

        String appendSql = " t.id in (" + String.join(",", idList) + ") ";

        try{
            Expression appendWhere = CCJSqlParserUtil.parseCondExpression(appendSql);

            Select select = (Select) CCJSqlParserUtil.parse(sql);
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            if (plainSelect.getWhere() == null) {
                plainSelect.setWhere(appendWhere);
            } else {
                plainSelect.setWhere(new AndExpression(appendWhere, plainSelect.getWhere()));
            }

            sql = plainSelect.toString();
        }catch (Exception e){
            log.error("【工作流】拼接SQL错误:{}-->{}", e.getMessage(), e.getStackTrace());
        }

        return sql;
    }

    private MappedStatement buildMappedStatement (MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    private void resourceCheck(){
        if(null == activitiProcessService){
            activitiProcessService = ApplicationContextProvider.getBean(ActivitiProcessService.class);
        }
    }

    static class BoundSqlSqlSource implements SqlSource {
        private final BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }
}
