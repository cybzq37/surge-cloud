package com.surge.common.mybatis.pagination;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;

@JsonView({Create.class, Query.class, Update.class})
public class PageInfo<T> extends Page<T> {

    private static final long serialVersionUID = 1L;

    public PageInfo() {}

    public PageInfo(long current, long size) {
        this(current, size, 0);
    }

    public PageInfo(long current, long size, long total) {
        this(current, size, total, true);
    }

    public PageInfo(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    public PageInfo(long current, long size, long total, boolean searchCount) {
        super(current, size, total, searchCount);
    }

    /* --------------- 以下为静态构造方式 --------------- */
    public static <T> Page<T> of(long current, long size) {
        return of(current, size, 0);
    }

    public static <T> Page<T> of(long current, long size, long total) {
        return of(current, size, total, true);
    }

    public static <T> Page<T> of(long current, long size, boolean searchCount) {
        return of(current, size, 0, searchCount);
    }

    public static <T> Page<T> of(long current, long size, long total, boolean searchCount) {
        return new PageDTO<>(current, size, total, searchCount);
    }

    @Override
    public String toString() {
        return "PageInfo{} " + super.toString();
    }
}
