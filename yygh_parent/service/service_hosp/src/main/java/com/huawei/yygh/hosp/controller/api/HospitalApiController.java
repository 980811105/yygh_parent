package com.huawei.yygh.hosp.controller.api;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.hosp.service.DepartmentService;
import com.huawei.yygh.hosp.service.HospitalService;
import com.huawei.yygh.hosp.service.HospitalSetService;
import com.huawei.yygh.hosp.service.ScheduleService;
import com.huawei.yygh.model.hosp.Hospital;
import com.huawei.yygh.model.hosp.Schedule;
import com.huawei.yygh.vo.hosp.HospitalQueryVo;
import com.huawei.yygh.vo.hosp.ScheduleOrderVo;
import com.huawei.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-20 09:41
 **/
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {
    @Resource
    private HospitalService hospitalService;
    @Resource
    private HospitalSetService hospitalSetService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private ScheduleService scheduleService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                                 @PathVariable Integer limit,
                                 HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.selectPage(page, limit, hospitalQueryVo);
        List<Hospital> content = hospitals.getContent();
        int totalPages = hospitals.getTotalPages();
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(@PathVariable String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }

    @ApiOperation(value = "根据医院编号获取科室列表")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode){
        return Result.ok(departmentService.findDeptTree(hoscode));
    }

    @ApiOperation(value = "根据医院编号获取医院预约挂号详情信息")
    @GetMapping("/findHospDetail/{hoscode}")
    public Result item(@PathVariable String hoscode){
        Map<String,Object> map = hospitalService.item(hoscode);
        return  Result.ok(map);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(@PathVariable String scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return Result.ok(schedule);
    }

    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {

        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @ApiOperation(value = "根据医院编号获取医院签名信息")
    @GetMapping("inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSignInfoVo(hoscode);
    }

}
