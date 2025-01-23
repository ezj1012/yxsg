package com.yxbear.sg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yxbear.sg.svc.play.bean.PlayInfo;

public class ExpandTest {

    private static Set<String> processedClasses = new HashSet<>(); // 避免重复处理相同的类

    public static void expand(Class<?> clazz) {
        if (clazz == null || !processedClasses.add(clazz.getName())) { return; }

        List<String> fieldNames = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Method[] methods = currentClass.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if ((methodName.startsWith("get") || methodName.startsWith("is")) && method.getParameterCount() == 0) {
                    String fieldName = extractFieldNameFromGetterOrIs(methodName);
                    if (findSetterMethod(currentClass, fieldName) != null) {
                        fieldNames.add(fieldName);
                    }
                } else if (methodName.startsWith("set") && method.getParameterCount() == 1) {
                    String fieldName = extractFieldNameFromSetter(methodName);
                    if (findGetterOrIsMethod(currentClass, fieldName) != null) {
                        fieldNames.add(fieldName);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        for (String fieldName : fieldNames) {
            try {
                Method getter = clazz.getMethod(getterName(fieldName), (Class<?>[]) null);
                Class<?> returnType = getter.getReturnType();
                System.out.println(fieldName + " (" + returnType.getSimpleName() + ")");

                // 如果字段不是基本类型，则递归展开
                if (!returnType.isPrimitive() && !returnType.getName().startsWith("java.lang.")) {
                    expand(returnType);
                }
            } catch (NoSuchMethodException e) {
                // 忽略异常，因为可能没有getter方法，或者方法签名不匹配
            }
        }
    }

    private static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String extractFieldNameFromGetterOrIs(String methodName) {
        if (methodName.startsWith("is")) {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        } else {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
    }

    private static String extractFieldNameFromSetter(String methodName) {
        return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    }

    private static Method findSetterMethod(Class<?> clazz, String fieldName) {
        try {
            return clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), Object.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findGetterOrIsMethod(Class<?> clazz, String fieldName) {
        try {
            return clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getMethod("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            } catch (NoSuchMethodException ex) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        expand(PlayInfo.class);
    }

}