package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ecoeler.app.bean.v1.CountOfDateBean;
import com.ecoeler.app.bean.v1.WebOverviewDataStatisticsBean;
import com.ecoeler.app.dto.v1.WebOverviewEchartsDto;
import com.ecoeler.app.dto.v1.QueryDateDto;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.mapper.AppUserMapper;
import com.ecoeler.app.mapper.DeviceMapper;
import com.ecoeler.app.mapper.WebOverviewMapper;
import com.ecoeler.app.service.WebOverviewDataService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.util.OverviewUtil;
import com.ecoeler.util.RatioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tangcx
 * @since 2020-09-23
 */
@Slf4j
@Service
public class WebOverviewServiceImpl implements WebOverviewDataService {
    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private WebOverviewMapper webOverviewMapper;

    /**
     * 查询统计数据
     * @return
     */
    @Override
    public WebOverviewDataStatisticsBean getDataStatistics()  {

        WebOverviewDataStatisticsBean bean = new WebOverviewDataStatisticsBean();
        int deviceTotal = deviceMapper.selectCount(null);
        OverviewUtil<Device> deviceOverviewUtil = new OverviewUtil<>();
        int todayDevice = deviceOverviewUtil.getDayCount(LocalDateTime.now(), deviceMapper);
        float deviceCompare = RatioUtil.getCompareRatio(deviceOverviewUtil.getDayCount(
                LocalDateTime.now().minusDays(1), deviceMapper), todayDevice);
        int userTotal = appUserMapper.selectCount(null);
        OverviewUtil<AppUser> appUserOverviewUtil = new OverviewUtil<>();
        int todayUser = appUserOverviewUtil.getDayCount(LocalDateTime.now(), appUserMapper);
        float userCompare = RatioUtil.getCompareRatio(appUserOverviewUtil.getDayCount(
                LocalDateTime.now().minusDays(1), appUserMapper), todayDevice);
        bean.setDeviceTotalCount(deviceTotal);
        bean.setDeviceTodayCount(todayDevice);
        bean.setDeviceDayCompare(deviceCompare);
        bean.setCustomerTodayCount(userTotal);
        bean.setCustomerTodayCount(todayUser);
        bean.setCustomerDayCompare(userCompare);
        return bean;
    }

    /**
     * 查询设备echarts 默认查询14天
     *
     * @param queryDateDto 查询时间
     * @return 查询时间段中每天对应新增注册设备数量列表
     */
    @Override
    public List<CountOfDateBean> getDeviceEcharts(QueryDateDto queryDateDto) {
        return getEchartsResult(queryDateDto, Device.class);
    }
    /**
     * 查询App用户echarts 默认查询14天
     *
     * @param queryDateDto 查询时间
     * @return 查询时间段中每天对应新增注册人员数量列表
     */
    @Override
    public List<CountOfDateBean> getAppUserEcharts(QueryDateDto queryDateDto) {
        return getEchartsResult(queryDateDto, AppUser.class);
    }

    /**
     * 封装查询时间段中每天不管数据库中是否有数据的总数
     * @param queryDateDto 查询条件
     * @param tableEntity 查询的表格
     * @return 查询时间段中每天对应的数量列表
     */
    private List<CountOfDateBean> getEchartsResult(QueryDateDto queryDateDto, Class tableEntity) {
        WebOverviewEchartsDto dto = getWebOverviewEchartsDto(queryDateDto);
        dto.setTableName(((TableName) tableEntity.getAnnotation(TableName.class)).value());
        List<CountOfDateBean> beans = webOverviewMapper.selectEcharsData(dto);
        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();
        //每天都有数据
        if (beans != null && beans.size() == (start.toEpochDay() - end.toEpochDay() + 1)) {
            return beans;
        }
        List<CountOfDateBean> result = new ArrayList<>();
        for (; start.isBefore(end.plusDays(1)); ) {
            String date = start.toString();
            //定义Date
            CountOfDateBean countOfDateBean = new CountOfDateBean(date);
            //查询到的有结果
            if (beans != null && beans.size() != 0) {
                for (CountOfDateBean bean : beans) {
                    //说明这天有数据
                    if (date.replace("-", "").equals(bean.getDate())) {
                        countOfDateBean.setCount(bean.getCount());
                    }
                }
            }
            result.add(countOfDateBean);
            start = start.plusDays(1L);
        }
        return result;
    }

    /**
     * 前台查询时间条件转成对数据库查询的条件
     * @param queryDateDto 前台查询数据
     * @return 数据库查询条件
     */
    private WebOverviewEchartsDto getWebOverviewEchartsDto(QueryDateDto queryDateDto) {
        String start = Optional.ofNullable(queryDateDto.getStartDate()).orElse("");
        String end = Optional.ofNullable(queryDateDto.getEndDate()).orElse("");
        LocalDate startDate;
        LocalDate endDate;
        if ("".equals(start.trim())) {
            if ("".equals(end.trim())) {
                //没有选择时间
                endDate = LocalDate.now();
                startDate = LocalDate.now().minusDays(WebOverviewEchartsDto.ECHARTS_DEFAULT_DATES);
            } else {
                //没有选择开始时间选择了结束时间
                endDate = LocalDate.parse(end);
                if (endDate.isAfter(LocalDate.now())) {
                    endDate = LocalDate.now();
                }
                startDate = endDate.minusDays(WebOverviewEchartsDto.ECHARTS_DEFAULT_DATES);
            }
        } else {
            if ("".equals(end.trim())) {
                //选择了开始时间没选择结束时间
                startDate = LocalDate.parse(start);
                endDate = startDate.plusDays(WebOverviewEchartsDto.ECHARTS_DEFAULT_DATES);
            } else {
                //选择开始以及结束时间
                startDate = LocalDate.parse(start);
                endDate = LocalDate.parse(end);
                if (startDate.until(endDate, ChronoUnit.DAYS) >= WebOverviewEchartsDto.ECHARTS_MAX_DAY) {
                    log.error("smart-home-service->WebOverviewServiceImpl->echarts max query days exceed 60");
                    throw new ServiceException(TangCode.CODE_ECHARTS_DAY_ERROR);
                }
                if (startDate.isAfter(endDate)) {
                    log.error("smart-home-service->WebOverviewServiceImpl->startDate after endDate");
                    throw new ServiceException(TangCode.START_TIME_AFTER_END_TIME);
                }
            }
        }
        if (endDate.isAfter(LocalDate.now())) {
            endDate = LocalDate.now();
        }
        WebOverviewEchartsDto webOverviewEchartsDto = new WebOverviewEchartsDto();
        webOverviewEchartsDto.setStartDate(startDate);
        webOverviewEchartsDto.setEndDate(endDate);
        return webOverviewEchartsDto;

    }
}
