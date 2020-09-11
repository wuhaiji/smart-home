package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.util.List;

/**
 * @author tangcx
 */
@Data
public class PageBean<T> {

    /**
     * 总数据量
     */
    private Long total;
    /**
     * 总页数
     */
    private Long pages;
    /**
     * 数据列表
     */
    private List<T> list;
}
