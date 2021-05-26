package com.huawei.yygh.hosp.controller.api;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.hosp.service.ScheduleService;
import com.huawei.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-17 10:16
 **/
@Api(tags = "查询排班规则接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Resource
    private ScheduleService scheduleService;

    @ApiOperation(value ="查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode) {
        Map<String, Object> map
                = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }

    @ApiOperation(value ="查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
