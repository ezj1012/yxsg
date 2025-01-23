package com.yxbear.core.db;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.yxbear.core.CommUtils;
import com.yxbear.core.bean.Condition;
import com.yxbear.core.bean.EntityBean;
import com.yxbear.core.db.mybatis.BaseMapper;
import com.yxbear.core.exception.DaoException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class TableInfo {

    public static final TableColumn ID_COLUMN = null;

    public static final TableColumn CREATE_TIME_COLUMN = null;

    public static final TableColumn MODIFY_TIME_COLUMN = null;

    Class<? extends EntityBean<? extends Number>> entityClass;

    Class<? extends Condition> cdtClass;

    Class<? extends Number> idClass;

    @Getter
    private List<TableColumn> columns;

    @Getter
    private TableColumn idColumn;

    @Getter
    private List<TableColumnCdt> cdts;

    @Getter
    private String[] columnNames;

    @Getter
    private String tableName;

    @SuppressWarnings("unchecked")
    public TableInfo(Class<?> mapperClass) {
        Type[] types = Stream.of(mapperClass.getGenericInterfaces()).filter(ParameterizedType.class::isInstance).map(ParameterizedType.class::cast).filter(type -> type.getRawType() == BaseMapper.class).findFirst().map(type -> type.getActualTypeArguments()).orElse(new Type[0]);
        try {
            init(Class.class.cast(types[1]), Class.class.cast(types[2]));
        } catch (Exception e) {
            throw new DaoException("Mapper type err!" + mapperClass.getClass());
        }
    }

    public TableInfo(Class<? extends EntityBean<? extends Number>> entityClass, Class<? extends Condition> cdtClass) {
        super();
        init(entityClass, cdtClass);
    }

    @SuppressWarnings("unchecked")
    private void init(Class<? extends EntityBean<? extends Number>> entityClass, Class<? extends Condition> cdtClass) {
        this.entityClass = entityClass;
        this.cdtClass = cdtClass;
        this.tableName = CommUtils.camelCaseToUnderscore(this.entityClass.getSimpleName());
        this.columns = Stream.of(entityClass.getDeclaredFields())
                //
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                //
                .map(field ->
                {
                    TableColumn tableColumn = new TableColumn(field);
                    if ("id".equals(field.getName())) {
                        idColumn = tableColumn;
                        idClass = (Class<? extends Number>) field.getType();
                    }
                    return tableColumn;
                })
                //
                .collect(Collectors.toList());

        Map<String, TableColumn> cdtFieldName = new HashMap<>();

        for (TableColumn col : columns) {
            col.getCdtName().forEach(name -> cdtFieldName.put(name, col));
        }

        columnNames = this.columns.stream().map(TableColumn::getColumnName).toArray(String[]::new);

        this.cdts = Stream.of(cdtClass.getDeclaredFields())
                //
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                //
                .map(f -> new TableColumnCdt(f, cdtFieldName.get(f.getName()))).collect(Collectors.toList());
    }

}
