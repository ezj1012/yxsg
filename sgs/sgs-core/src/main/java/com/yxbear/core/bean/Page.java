package com.yxbear.core.bean;

import java.util.List;
import java.util.function.Function;

import lombok.Data;

@Data
public class Page<D> {

    private long pageNum;

    private long pageSize;

    private long totalRows;

    private long totalPages;

    private List<D> data;

    public Page() {
    }

    public Page(long pageNum, long pageSize, long totalRows, long totalPages, List<D> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRows = totalRows;
        this.totalPages = totalPages;
        this.data = data;
    }

    public Page(Page<?> page, Function<List<?>, List<D>> conver) {
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.totalRows = page.getTotalRows();
        this.totalPages = page.getTotalPages();
        this.data = conver.apply(page.getData());
    }

}
