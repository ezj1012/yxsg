package com.yxbear.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommUtils {

    public static final DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter YYYYMMDDHHMMSS2_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");

    public static Long curDateTime() {
        return Long.parseLong(YYYYMMDDHHMMSS_FORMATTER.format(LocalDateTime.now()));
    }

    public static String format(DateTimeFormatter f, long epochMilli) {
        return f.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()));
    }

    public static String camelCaseToUnderscore(String className) {
        return camelCaseToUnderscore(className, false);
    }

    public static String camelCaseToUnderscore(String className, boolean lowerCase) {
        StringBuilder tableName = new StringBuilder();
        for (int i = 0; i < className.length(); i++) {
            char ch = className.charAt(i);

            if (Character.isUpperCase(ch) && i > 0) {
                tableName.append('_');
            }
            if (lowerCase) {
                tableName.append(Character.toLowerCase(ch));
            } else {
                tableName.append(Character.toUpperCase(ch));
            }
        }
        return tableName.toString();
    }

    public static boolean isEmpty(String obj) {
        return obj == null || "".equals(obj.trim());
    }

    public static String upFirstChar(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }
    //
    // public static List<Field> getCfgFields(Class<?> clz) {
    // List<Field> cfgFields = new ArrayList<>();
    // ReflectionUtils.doWithFields(clz, f -> {
    // f.setAccessible(true);
    // cfgFields.add(f);
    // }, f -> f.getAnnotation(Cfg.class) != null);
    // return cfgFields;
    // }

    // public static String getCfgFieldValue(Field f, Object s) {
    // try {
    // Object v = f.get(s);
    // if (v == null) {
    // String defaultValue = f.getAnnotation(Cfg.class).defaultValue();
    // if ((defaultValue == null || "".equals(defaultValue)) && Number.class.isAssignableFrom(f.getType())) { return "0"; }
    // return defaultValue;
    // } else if (v instanceof Boolean) {
    // return (boolean) v ? "1" : "0";
    // } else {
    // return v.toString();
    // }
    // } catch (Exception e) {
    // return "";
    // }
    // }

}
