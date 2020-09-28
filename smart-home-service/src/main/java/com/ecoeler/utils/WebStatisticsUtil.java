package com.ecoeler.utils;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.WebStatistics;
import com.ecoeler.app.mapper.WebStatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

/**
 * 数量查询的接口
 * @author TangCx

 */
@Slf4j
@Component
public class WebStatisticsUtil {

    @Autowired
    private WebStatisticsMapper webStatisticsMapper;

    public void updateStatistics(Class updateClass){
        //插入统计表数据
        LocalDate now= LocalDate.now();
        WebStatistics webStatistics=webStatisticsMapper.selectOne(new LambdaQueryWrapper<WebStatistics>()
                .eq(WebStatistics::getDate,now.toString()));
        if (webStatistics!=null){
            //说明存在
            if (updateClass== Device.class){
                webStatistics.setDeviceNumber(webStatistics.getDeviceNumber()+1);
            }
            if (updateClass== AppUser.class){
                webStatistics.setUserNumber(webStatistics.getUserNumber()+1);
            }
            webStatistics.setDate(null);
            webStatisticsMapper.updateById(webStatistics);
        }else {
            //不存在
            webStatistics=new WebStatistics(now.toString());
            if (updateClass== Device.class){
                webStatistics.setDeviceNumber(1);
            }
            if (updateClass== AppUser.class){
                webStatistics.setUserNumber(1);
            }
            webStatisticsMapper.insert(webStatistics);
        }

    }

}
