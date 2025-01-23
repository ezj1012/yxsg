package com.yxbear.core.db.mybatis;

import java.util.Collection;

import org.apache.ibatis.jdbc.SQL;

import com.yxbear.core.CommUtils;
import com.yxbear.core.bean.Condition;
import com.yxbear.core.bean.EntityBean;
import com.yxbear.core.db.ConditionType;
import com.yxbear.core.db.TableColumnCdt;
import com.yxbear.core.db.TableInfo;
import com.yxbear.core.exception.DaoException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBatisTableInfo {

    static final String ID_CDT = "id=#{id}";

    String databaseId;

    TableInfo tableInfo;

    public MyBatisTableInfo(String databaseId, TableInfo tableInfo) {
        super();
        this.databaseId = databaseId;
        this.tableInfo = tableInfo;
    }

    public TableInfo getTabelInfo() {
        return tableInfo;
    }

    public SQL select() {
        return new SQL()
                //
                .SELECT(tableInfo.getColumnNames())
                //
                .FROM(tableInfo.getTableName());
    }

    public SQL selectCount() {
        return new SQL()
                //
                .SELECT("count(1)")
                //
                .FROM(tableInfo.getTableName());
    }

    public SQL selectBy(Condition cdt, String orders) {
        SQL sql = where(select(), cdt);
        return CommUtils.isEmpty(orders) ? sql : sql.ORDER_BY(orders);
    }

    public SQL countBy(Condition cdt) {
        return where(selectCount(), cdt);
    }

    public SQL selectById() {
        return select().WHERE(ID_CDT);
    }

    public SQL save(EntityBean<?> bean) {
        SQL sql = new SQL().INSERT_INTO(tableInfo.getTableName());
        tableInfo.getColumns().forEach(col -> {
            try {
                if (col.getField().get(bean) != null) {
                    sql
                            //
                            .INTO_COLUMNS(col.getColumnName())
                            //
                            .INTO_VALUES("#{entity." + col.getFieldName() + "}");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return sql;
    }

    public SQL updateById(Number id, EntityBean<?> bean) {
        SQL sql = new SQL().UPDATE(tableInfo.getTableName());
        tableInfo.getColumns().forEach(col -> {
            try {
                if (col.getField().get(bean) != null && !col.getField().getName().equals("id")) {
                    sql.SET(col.getColumnName() + " = #{entity." + col.getFieldName() + "}");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        sql.WHERE("id = #{id}");
        return sql;
    }

    public SQL deleteById(Number id) {
        return new SQL().DELETE_FROM(tableInfo.getTableName()).WHERE("id = #{id}");
    }

    public SQL deleteByIds(Collection<? extends Number> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("id in (");
        if (!ids.isEmpty()) {
            for (Number i : ids) {
                sb.append(i).append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append(")");

        return new SQL().DELETE_FROM(tableInfo.getTableName()).WHERE(sb.toString());
    }

    public SQL deleteBy(Condition cdt) {
        return where(new SQL().DELETE_FROM(tableInfo.getTableName()), cdt);
    }

    private SQL where(SQL sql, Condition cdt) {
        if (cdt == null) { return sql; }
        this.tableInfo.getCdts().forEach(c -> {
            try {
                Object val = c.getField().get(cdt);
                if (val == null) { return; }

                StringBuilder se = new StringBuilder();
                se.append(c.getEntityField().getColumnName())
                        // op
                        .append(c.getType().toSql(databaseId))
                        //
                        .append(c.isArr() ? arrValueToString(c, (Object[]) val) : valueToString(c));
                sql.WHERE(se.toString());
            } catch (Exception e) {
                throw new DaoException("cdt err!", e);
            }
        });
        return sql;
    }

    private String valueToString(TableColumnCdt c) {
        ConditionType type = c.getType();
        if (type == ConditionType.LIKE) {
            return " CONCAT('%',#{cdt." + c.getField().getName() + "},'%')";
        } else {
            return "#{cdt." + c.getField().getName() + "}";
        }
    }

    private String arrValueToString(TableColumnCdt c, Object[] arr) {
        StringBuilder sb = new StringBuilder();
        String name = c.getField().getName();
        sb.append("(");
        for (int i = 0; i < arr.length; i++) {
            sb.append("#{cdt.").append(name).append("[").append(i).append("]},");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append(")");
        return sb.toString();
    }

}
