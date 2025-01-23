package com.yxbear.core.db.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.github.pagehelper.page.PageMethod;
import com.yxbear.core.bean.Condition;
import com.yxbear.core.bean.EntityBean;
import com.yxbear.core.bean.Page;

public interface BaseMapper<I extends Number, T extends EntityBean<I>, C extends Condition> {

    @SelectProvider
    T selectById(@Param("id") I id);

    default List<T> queryAll() {
        return queryList(null, "ID ");
    }

    @SelectProvider
    List<T> queryList(@Param("cdt") C cdt, @Param("orders") String orders);

    default Page<T> queryPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("cdt") C cdt, @Param("orders") String orders) {
        com.github.pagehelper.Page<Object> page = PageMethod.startPage(pageNum, pageSize, orders);
        List<T> datas = this.queryList(cdt, orders); // 这里的 datas == page
        return new Page<>(pageNum, pageSize, page.getTotal(), page.getPages(), new ArrayList<>(datas));
    }

    default List<T> queryPageList(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("cdt") C cdt, @Param("orders") String orders) {
        PageMethod.startPage(pageNum, pageSize, orders);
        return new ArrayList<>(this.queryList(cdt, orders));
    }

    @SelectProvider
    Long count(@Param("cdt") C cdt);

    default I saveOrUpdate(T t) {
        if (t.getId() == null) {
            save(t);
            return t.getId();
        } else {
            T old = selectById(t.getId());
            if (old == null) {
                save(t);
            } else {
                updateById(t.getId(), t);
            }
        }
        return t.getId();
    }

    @InsertProvider
    void save(@Param("entity") T t);

    @UpdateProvider
    void updateById(@Param("id") I id, @Param("entity") T t);

    default Integer saveOrUpdateBatch(List<T> list) {
        list.forEach(this::saveOrUpdate);
        return list.size();
    }

    @DeleteProvider
    Integer deleteById(@Param("id") I id);

    @DeleteProvider
    Integer deleteByIds(@Param("ids") Collection<I> ids);

    default Integer deleteByIds(I[] ids) {
        return deleteByIds(Arrays.asList(ids));
    }

    @DeleteProvider
    Integer deleteByCdt(@Param("cdt") C cdt);

}
