package com.yxbear.sg.coder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.yxbear.sg.svc.play.bean.CityBuilding;

public class ToTsInterface {

    public static void main(String[] args) {
        // print(ImgNode.class);
//        print(CfgTechnicCondition.class);
//        print(CfgTechnic.class);
        print(CityBuilding.class);
    }

    public static void print(Class<?> cls) {

        Field[] declaredFields = cls.getDeclaredFields();
        System.out.println("export interface " + cls.getSimpleName().replace("Entity", "") + " {");
        Class<?> superclass = cls.getSuperclass();
        if (superclass != null) {
            printFileds(superclass.getDeclaredFields());
        }
        printFileds(declaredFields);
        System.out.println("}");
    }

    private static void printFileds(Field[] declaredFields) {
        for (Field field : declaredFields) {
            String type = field.getType().getSimpleName().toLowerCase();
            if (type.equals("list")) {
                ParameterizedType tt = (ParameterizedType) field.getGenericType();

                Type ta = tt.getActualTypeArguments()[0];

                type = ((Class<?>) ta).getSimpleName() + "[]";
            } else if (type.equals("long") || type.equals("integer") || type.equals("int")) {
                type = "number";
            } else if (!type.equals("string")) {
                type = field.getType().getSimpleName();
            }
            String t = field.getName() + ": " + type;
            System.out.println("    " + t);
        }
    }
}
