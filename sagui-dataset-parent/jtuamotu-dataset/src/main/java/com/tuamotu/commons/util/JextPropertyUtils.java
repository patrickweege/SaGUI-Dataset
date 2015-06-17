package com.tuamotu.commons.util;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;


public class JextPropertyUtils {

    public static String getPropertyNameFromMethod(String methodName) {
        if (StringUtils.startsWithIgnoreCase(methodName, "set") || StringUtils.startsWithIgnoreCase(methodName, "get")) {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
        return null;
    }

    public static String getPropertyNameFromMethod(Method method) {
        return JextPropertyUtils.getPropertyNameFromMethod(method.getName());
    }

}
