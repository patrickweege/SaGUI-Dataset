package com.sagui.dataset.commons.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


@SuppressWarnings("rawtypes")
public class AnnotationUtils {
    
    private static AnnotationUtils instance = new AnnotationUtils();
    
    
    public static List<Method> getAnnotatedMethods(Class clazz, Class<? extends Annotation> annotation) {
        ArrayList<Method> annotatedMethods = new ArrayList<Method>(); 
        instance.getAnnotatedDeclaredMethods(clazz, annotation, false, Modifier.PUBLIC, annotatedMethods);
        return annotatedMethods;
    }
    
    public static List<Method> getAnnotatedMethods(Class clazz, Class<? extends Annotation> annotation, boolean recursively) {
        ArrayList<Method> annotatedMethods = new ArrayList<Method>();
        instance.getAnnotatedDeclaredMethods(clazz, annotation, recursively, Modifier.PUBLIC, annotatedMethods);
        return annotatedMethods;
    }

    private Method[] getDeclaredMethods(Class clazz, boolean recursively) {
        List<Method> methods = new LinkedList<Method>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Collections.addAll(methods, declaredMethods);
        
        Class superClass = clazz.getSuperclass();

        if (superClass != null && recursively) {
            Method[] superDeclaredMethods = getDeclaredMethods(superClass, recursively);
            if (superDeclaredMethods.length > 0) Collections.addAll(methods, superDeclaredMethods);
        }

        return methods.toArray(new Method[methods.size()]);
    }

    private void getAnnotatedDeclaredMethods(Class clazz, Class<? extends Annotation> annotationClass, boolean recursively, int modifier, List<Method> methods) {
        Method[] allMethods = getDeclaredMethods(clazz, recursively);
        for (Method method : allMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                if(modifier == -1 || (modifier & method.getModifiers()) > 0) {
                    methods.add(method);
                }
            }
        }
    }
}
