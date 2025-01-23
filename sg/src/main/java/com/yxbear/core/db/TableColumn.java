package com.yxbear.core.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.yxbear.core.CommUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class TableColumn {

    @Getter
    private Field field;

    @Getter
    private String columnName;

    public TableColumn(Field field) {
        this.field = field;
        field.setAccessible(true);
        columnName = CommUtils.camelCaseToUnderscore(getFieldName());
    }

    public String getFieldName() {
        return field.getName();
    }

    public List<String> getCdtName() {
        List<String> r = new ArrayList<>();
        String type = field.getType().getSimpleName();
        String name = field.getName();
        r.add(name);
        r.add(name + "s");
        if ("String".equals(type)) {
            r.add(name + "Equal");
        } else if ("Integer".equals(type) || "Long".equals(type) || "Short".equals(type)) {
            String fName = CommUtils.upFirstChar(field.getName());
            r.add(name + "s");
            r.add("start" + fName);
            r.add("end" + fName);
        } else if ("Boolean".equalsIgnoreCase(type)) {

        }
        return r;
    }

}
