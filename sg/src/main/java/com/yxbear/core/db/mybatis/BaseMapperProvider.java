package com.yxbear.core.db.mybatis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

import com.github.pagehelper.page.PageMethod;
import com.yxbear.core.CommUtils;
import com.yxbear.core.bean.Auditable;
import com.yxbear.core.bean.Condition;
import com.yxbear.core.bean.EntityBean;
import com.yxbear.core.db.CommIdGenerator;
import com.yxbear.core.db.IdGenerator;
import com.yxbear.core.db.TableColumn;
import com.yxbear.core.db.TableInfo;
import com.yxbear.core.exception.DaoException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseMapperProvider implements ProviderMethodResolver {

    static final Map<Class<?>, TableInfo> TABLE_INFO_MAP = new HashMap<>();

    static final Map<String, MyBatisTableInfo> MY_BATIS_TABLE_INFO_MAP = new HashMap<>();

    static IdGenerator idGen = new CommIdGenerator();

    private static MyBatisTableInfo tableInfo(ProviderContext context) {
        final String KEY = context.getDatabaseId() + context.getMapperType().getName();
        MyBatisTableInfo myBatisTableInfo = MY_BATIS_TABLE_INFO_MAP.get(KEY);

        if (myBatisTableInfo == null) {
            synchronized (BaseMapperProvider.class) {
                myBatisTableInfo = MY_BATIS_TABLE_INFO_MAP.computeIfAbsent(KEY, (String k) -> new MyBatisTableInfo(context.getDatabaseId(), getTableInfo(context.getMapperType())));
            }
        }
        if (myBatisTableInfo == null) { throw new DaoException("MapperType Err! " + context.getMapperType()); }
        return myBatisTableInfo;
    }

    private static TableInfo getTableInfo(Class<?> cls) {
        TableInfo tableInfo = TABLE_INFO_MAP.get(cls);

        if (tableInfo == null) {
            synchronized (BaseMapperProvider.class) {
                try {
                    tableInfo = TABLE_INFO_MAP.computeIfAbsent(cls, TableInfo::new);
                } catch (Exception e) {
                    throw new DaoException("MapperType Err! " + cls.getName(), e);
                }
            }
        }
        return tableInfo;
    }

    public static String selectById(ProviderContext context) {
        String sql = tableInfo(context).selectById().toString();
        log.debug(sql);
        return sql;
    }

    public static String queryList(Condition cdt, String orders, ProviderContext context) {
        String sql = tableInfo(context).selectBy(cdt, orders).toString();
        log.debug(sql);
        return sql;
    }

    public static String queryPageList(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("cdt") Condition cdt, @Param("orders") String orders, ProviderContext context) {
        PageMethod.startPage(pageNum, pageSize, orders);
        return queryList(cdt, orders, context);
    }

    public static String save(@Param("entity") EntityBean<?> bean, ProviderContext context) {
        MyBatisTableInfo tableInfo = tableInfo(context);
        if (bean.getId() == null) {
            try {
                TableColumn idColumn = tableInfo.getTabelInfo().getIdColumn();
                Number id = idGen.id(tableInfo.getTabelInfo());
                idColumn.getField().set(bean, id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DaoException("表[" + tableInfo.getTabelInfo().getTableName() + "]ID生成失败!");
            }
        }
        if (bean instanceof Auditable aer) {
            Long createTime = CommUtils.curDateTime();
            if (aer.getCreateTime() == null) {
                aer.setCreateTime(createTime);
            }
            if (aer.getModifyTime() == null) {
                aer.setModifyTime(createTime);
            }
        }
        String sql = tableInfo.save(bean).toString();
        log.debug(sql);
        return sql;
    }

    public static String updateById(@Param("id") Number id, @Param("entity") EntityBean<?> bean, ProviderContext context) {
        if (id == null) { throw new DaoException("id 不能为空"); }
        if (bean instanceof Auditable aer) {
            aer.setModifyTime(CommUtils.curDateTime());
        }
        String sql = tableInfo(context).updateById(id, bean).toString();
        log.debug(sql);
        return sql;
    }

    public static String count(Condition cdt, ProviderContext context) {
        String sql = tableInfo(context).countBy(cdt).toString();
        log.debug(sql);
        return sql;
    }

    public static String deleteById(@Param("id") Number id, ProviderContext context) {
        String sql = tableInfo(context).deleteById(id).toString();
        log.debug(sql);
        return sql;
    }

    public static String deleteByIds(@Param("ids") Collection<? extends Number> ids, ProviderContext context) {
        String sql = tableInfo(context).deleteByIds(ids).toString();
        log.debug(sql);
        return sql;
    }

    public static String deleteByCdt(@Param("cdt") Condition cdt, ProviderContext context) {
        String sql = tableInfo(context).deleteBy(cdt).toString();
        log.debug(sql);
        return sql;
    }

}
