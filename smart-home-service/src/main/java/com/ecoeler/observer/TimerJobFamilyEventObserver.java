package com.ecoeler.observer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.dto.v1.FamilyDto;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.mapper.TimerJobMapper;
import com.ecoeler.app.service.ISceneService;
import com.ecoeler.app.service.ITimerJobService;
import com.ecoeler.constant.FamilyTypeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujihong
 */
@Component
public class TimerJobFamilyEventObserver implements FamilyEventObserver {

    @Autowired
    private TimerJobMapper timerJobMapper;

    @Autowired
    private ITimerJobService timerJobService;

    /**
     * 当删除家庭时，定时任务需要做的删除操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:35 2020-09-28
     */
    @Override
    public void whenUserDeleteFamily(UserFamilyDto userFamilyDto) {
        List<Long> timerJobIdList;
        QueryWrapper<TimerJob> timerJobQueryWrapper = new QueryWrapper<>();

        // 删除定时任务
        timerJobQueryWrapper.eq("family_id", userFamilyDto.getFamilyId());
        timerJobQueryWrapper.select("id");
        timerJobIdList = timerJobMapper.selectList(timerJobQueryWrapper).stream().map(TimerJob::getId).collect(Collectors.toList());
        for (Long timerJobId: timerJobIdList) {
            timerJobService.deleteJob(timerJobId);
        }
    }

    /**
     * 当用户离开家庭时，定时任务需要做的操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserLeaveFamily(UserFamilyDto userFamilyDto) {

    }
}
