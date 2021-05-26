package com.huawei.yygh.hosp.service;

import com.huawei.yygh.model.hosp.Schedule;
import com.huawei.yygh.vo.hosp.ScheduleOrderVo;
import com.huawei.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:56
 **/
public interface ScheduleService {

    void save(Map<String, Object> paramMap);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    Map<String,Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getScheduleById(String scheduleId);

    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    void update(Schedule schedule);
}
