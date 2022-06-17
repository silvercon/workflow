package com.newfiber.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * @author : X.K
 * @since : 2022/4/1 下午5:13
 */

@Configuration
public class FlowableEngineConfig implements ProcessEngineConfigurationConfigurer {

	@Override
	public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
		springProcessEngineConfiguration.setActivityFontName("宋体");
		springProcessEngineConfiguration.setAnnotationFontName("宋体");
		springProcessEngineConfiguration.setLabelFontName("宋体");
	}
}
