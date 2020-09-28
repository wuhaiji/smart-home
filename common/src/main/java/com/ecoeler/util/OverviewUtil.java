package com.ecoeler.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 数量查询的接口
 * @author TangCx
 * @param <T>
 */
public class OverviewUtil<T> {

    /**
     * 查询一天的数量
     *
     * @param queryTime
     * @param baseMapper
     * @return
     */
    public int getDayCount(LocalDateTime queryTime, BaseMapper<T> baseMapper) {
        LocalDateTime first = TimeUtil.getEarliestTimeOfDay(queryTime);
        LocalDateTime last = TimeUtil.getLatestTimeOfDay(queryTime);
        return getDefineCount(first, last, baseMapper);
    }

    /**
     * 查询日环比
     */
    public float getDayCompare(BaseMapper<T> baseMapper) {
        int today = getDayCount(LocalDateTime.now(), baseMapper);
        int yesterday = getDayCount(LocalDateTime.now().minusDays(1), baseMapper);
        return RatioUtil.getCompareRatio(yesterday, today);
    }

    /**
     * 查询自定义时间长短的数量
     *
     * @param start      开始时间
     * @param end        结束时间
     * @param baseMapper 查询那个表
     * @return 在自定义时间内的数量
     */
    private int getDefineCount(LocalDateTime start, LocalDateTime end, BaseMapper<T> baseMapper) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("create_time", start, end);
        Integer count = baseMapper.selectCount(queryWrapper);
        return Optional.ofNullable(count).orElse(0);
    }
}
