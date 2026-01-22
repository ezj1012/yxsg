package com.yxbear.core.db;

import java.lang.reflect.Field;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class TableColumnCdt {

    private String fieldName;

    @Getter
    private Field field;

    @Getter
    private TableColumn entityField;

    @Getter
    private ConditionType type;

    @Getter
    private boolean string;

    @Getter
    private boolean arr;

    public TableColumnCdt(Field cdtField, TableColumn entityField) {
        this.entityField = entityField;
        this.field = cdtField;
        this.field.setAccessible(true);
        this.type = ConditionType.type(this.entityField.getField(), this.field);
        arr = cdtField.getType().isArray();
        string = this.entityField.getField().getType() == String.class;
    }

}
