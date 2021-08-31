package com.newfiber.config;

import javax.sql.DataSource;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;

//@Configuration
public class ProcessEngineConfig extends AbstractProcessEngineAutoConfiguration {

//    @Bean
//    @ConfigurationProperties(prefix = "newfiber.workflow.datasource")
    public DataSource activitiDataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
    public ProcessEngine createProcessEngine(){
        ProcessEngineConfiguration processEngineConfiguration= ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        return processEngineConfiguration.buildProcessEngine();
    }

}
