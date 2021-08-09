package com.newfiber.workflow.support.page;

import cn.hutool.core.util.ReflectUtil;
import com.newfiber.workflow.service.ActivitiProcessService;
import com.newfiber.workflow.support.IWorkflowCallback;
import com.newfiber.workflow.utils.ApplicationContextProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;
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
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),}
)
public class WorkflowPageInterceptor implements Interceptor{

    private ActivitiProcessService activitiProcessService;

    private final Map<Class<?>, EntityInfo> classEntityInfoMap = new HashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        WorkflowPage workflowPage = WorkflowPageHelper.getWorkflowPage();
        if(null == workflowPage){
            return invocation.proceed();
        }

        if((StringUtils.isBlank(workflowPage.getTaskKey()) && StringUtils.isBlank(workflowPage.getUserId()))){
            return invocation.proceed();
        }

        resourceCheck();

        List<String> businessKeyList = activitiProcessService.listTodoBusinessKeyByUser(
                workflowPage.getWorkflowCallback().getWorkflowDefinition().getWorkflowKey(),
                workflowPage.getTaskKey(), workflowPage.getUserId());
        List<String> taskDoneBusinessKeyList = activitiProcessService.listTaskDoneBusinessKeyByUser(workflowPage.getWorkflowCallback().getWorkflowDefinition().getWorkflowKey(),
                workflowPage.getTaskKey(), workflowPage.getUserId());
        businessKeyList.addAll(taskDoneBusinessKeyList);

        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);

        String executeSql = boundSql.getSql();
        executeSql = appendWhereIdCondition(executeSql, businessKeyList, workflowPage.getWorkflowCallback());

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
        WorkflowPageHelper.clearPage();

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String appendWhereIdCondition(String sql, List<String> idList, IWorkflowCallback<?> workflowCallback){
        if(CollectionUtils.isEmpty(idList)){
            idList.add("0");
        }

        String appendSql = " %s.id in ( %s ) ";
        appendSql = String.format(appendSql, workflowCallback.getWorkflowDefinition().getTableName(),
                parseIdListByIdType(workflowCallback.getWorkflowDefinition().getTableIdType(), idList));

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

    /**
     * 基于MyBatisPlus解析表名
     * @param entityClass 实体
     * @return 表名
     */
    @SuppressWarnings("unchecked")
    private String parseTableName(Class<?> entityClass) {
        String tableName = "t";
        if(null == entityClass){
            return tableName;
        }

        EntityInfo entityInfo = classEntityInfoMap.get(entityClass);
        if(null != entityInfo && null != entityInfo.getTableName()){
            tableName = entityInfo.getTableName();
        }else{
            Annotation[] annotations = entityClass.getAnnotations();
            for(Annotation annotation : annotations){
                if("TableName".equals(annotation.annotationType().getSimpleName())){
                    try{
                        Object o = ReflectUtil.getFieldValue(annotation, "h");
                        if(o instanceof InvocationHandler){
                            LinkedHashMap<String, Object> memberValues = (LinkedHashMap<String, Object>) ReflectUtil.getFieldValue(o, "memberValues");
                            tableName = memberValues.get("value").toString();
                            classEntityInfoMap.put(entityClass, new EntityInfo(tableName));
                        }
                    }catch (Exception ignore){ }
                }
            }
        }

        return tableName;
    }

    /**
     * 基于MyBatisPlus解析主键编号
     * @param entityClass 实体
     * @param idList 编号列表
     * @return 编号
     */
    private String parseIdList(Class<?> entityClass, List<String> idList){
        if(CollectionUtils.isEmpty(idList)){
            idList.add("0");
        }
        String idListString = String.join(",", idList);

        if(null == entityClass){
            return idListString;
        }

        Class<?> tableIdType = null;
        EntityInfo entityInfo = classEntityInfoMap.get(entityClass);

        if(null != entityInfo && null != entityInfo.getTableIdType()){
            tableIdType = entityInfo.getTableIdType();
        }else{
            Field[] fields = entityClass.getDeclaredFields();
            for(Field field : fields){
                Annotation[] annotations = field.getAnnotations();
                for(Annotation annotation : annotations){
                    if("TableId".equals(annotation.annotationType().getSimpleName())){
                        tableIdType = field.getType();

                        if(null != classEntityInfoMap.get(entityClass)){
                            classEntityInfoMap.get(entityClass).setTableIdType(tableIdType);
                        }else{
                            classEntityInfoMap.put(entityClass, new EntityInfo(tableIdType));
                        }

                    }
                }
            }
        }

        if(ReflectUtil.newInstance(tableIdType, "0") instanceof String){
            idListString = String.join("','", idList);
            idListString = "'" + idListString + "'";
        }

        return idListString;
    }

    private String parseIdListByIdType(Class<?> tableIdType, List<String> idList){
        String idListString = String.join(",", idList);

        if(null != tableIdType && ReflectUtil.newInstance(tableIdType, "0") instanceof String){
            idListString = String.join("','", idList);
            idListString = "'" + idListString + "'";
        }

        return idListString;
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

    @Data
    static class EntityInfo{
        String tableName;
        Class<?> tableIdType;

        public EntityInfo(String tableName) {
            this.tableName = tableName;
        }

        public EntityInfo(Class<?> tableIdType) {
            this.tableIdType = tableIdType;
        }
    }
}
