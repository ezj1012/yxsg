package com.yxbear.core.bean;

/**
 * 
 * @author Administrator
 *
 * @param <E>
 */
public class QueryPageCondition<E> {

    private Integer pageNum;

    private Integer pageSize;

    private E cdt;

    private String orders;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public E getCdt() {
        return cdt;
    }

    public void setCdt(E cdt) {
        this.cdt = cdt;
    }

}
