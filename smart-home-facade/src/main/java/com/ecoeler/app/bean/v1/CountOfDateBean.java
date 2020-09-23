package com.ecoeler.app.bean.v1;


import lombok.Data;

import java.time.LocalDate;

/**
 * @author TangCX
 */
@Data
public class CountOfDateBean {
    /**
     * 数量
     */
    private long count;
    /**
     * 日期
     */
    private String date;

    public CountOfDateBean() {
    }

    public CountOfDateBean(String date) {
        this.date = date;
    }
}
