package com.yxbear.core.db;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.UnaryOperator;

public enum ConditionType {

    //
    EQ(dbid -> " = "),
    //
    LIKE(dbid -> " like "),
    //
    START(dbid -> " >= "),
    //
    END(dbid -> " <= "),
    //
    IN(dbid -> " in ")
    //
    ;

    private UnaryOperator<String> toSql;

    private ConditionType(UnaryOperator<String> toSql) {
        this.toSql = toSql;
    }

    public String toSql(String dbId) {
        return toSql.apply(dbId);
    }

    public static ConditionType type(Field entityField, Field conditionField) {
        Class<?> type = conditionField.getType();
        boolean isString = type == String.class;

        if (type.isArray()) {
            return IN;
        } else if (isString && Objects.equals(conditionField.getName(), entityField.getName())) {
            //
            return LIKE;
        } else if (isString || Objects.equals(conditionField.getName(), entityField.getName())) {
            return EQ;
        } else if (conditionField.getName().startsWith("start")) {
            return START;
        } else {
            return END;
        }
    }

}
