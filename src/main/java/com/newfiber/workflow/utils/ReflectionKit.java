package com.newfiber.workflow.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionKit {

    public static Class<?> getInterfaceGeneric(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        Type entityType = types[0];
        if(entityType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) entityType;
            Type type = parameterizedType.getActualTypeArguments()[0];
            return (Class<?>) type;
        }
        return null;
    }

}
