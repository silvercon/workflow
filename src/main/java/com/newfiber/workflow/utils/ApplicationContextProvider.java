package com.newfiber.workflow.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author : haiqingzheng
 * @since : 2019-03-25 13:35
 */

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;


    private ApplicationContextProvider() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> aClass) {
        return context.getBean(name, aClass);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T) context.getBean(clz);
    }

}
