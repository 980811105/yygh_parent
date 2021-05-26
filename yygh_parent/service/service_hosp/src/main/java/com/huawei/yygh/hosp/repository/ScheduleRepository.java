package com.huawei.yygh.hosp.repository;

import com.huawei.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-05-11 16:51
 **/
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> getScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
