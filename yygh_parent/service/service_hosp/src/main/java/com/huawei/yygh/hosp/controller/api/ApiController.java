package com.huawei.yygh.hosp.controller.api;


import com.huawei.yygh.common.exception.YyghException;
import com.huawei.yygh.common.helper.HttpRequestHelper;
import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.common.result.ResultCodeEnum;
import com.huawei.yygh.common.utils.MD5;
import com.huawei.yygh.hosp.service.DepartmentService;
import com.huawei.yygh.hosp.service.HospitalService;
import com.huawei.yygh.hosp.service.HospitalSetService;
import com.huawei.yygh.hosp.service.ScheduleService;
import com.huawei.yygh.hosp.utils.CheckKey;
import com.huawei.yygh.model.hosp.Department;
import com.huawei.yygh.model.hosp.Hospital;
import com.huawei.yygh.model.hosp.Schedule;
import com.huawei.yygh.vo.hosp.DepartmentQueryVo;
import com.huawei.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:59
 **/
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Resource
    private HospitalService hospitalService;
    @Resource
    private HospitalSetService hospitalSetService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private ScheduleService scheduleService;

    CheckKey checkKey = new CheckKey();

    @ApiOperation("删除排班信息")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = checkKey.getParamMap(request);
        String hoscode = (String)paramMap.get("hoscode");
        String hosScheduleId  = (String) paramMap.get("hosScheduleId ");
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId );
        return Result.ok();
    }

    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("schedule/list")
    public Result schedule(HttpServletRequest request) {
        Map<String, Object> paramMap = checkKey.getParamMap(request);
        String hoscode = (String)paramMap.get("hoscode");
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 1 : Integer.parseInt((String)paramMap.get("limit"));
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Schedule> pageModel = scheduleService.selectPage(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("上传排班信息")
    @PostMapping("saveSchedule")
    public Result savaSchedule(HttpServletRequest request){
        Map<String, Object> paramMap = checkKey.getParamMap(request);
        String hoscode = (String)paramMap.get("hoscode");
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("删除科室信息")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    @ApiOperation("查询科室接口/获取分页列表")
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)paramMap.get("hoscode");
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 1 : Integer.parseInt((String)paramMap.get("limit"));
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("上传科室接口")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        //获取传递过来科室信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)paramMap.get("hoscode");
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用service的方法
        departmentService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "查询医院信息")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取医院编号
        String hoscode = (String)paramMap.get("hoscode");
        if(!(checkKey.flagCheck(request,hospitalSetService,hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @ApiOperation("上传医院信息")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1、获取医院系统传递过来的签名，签名进行MD5加密
        String hospSign = (String) paramMap.get("sign");
        //2、根据传递过来的医院编号，查询数据库签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3、把数据库查询签名进行MD5进行加密
        String signKeyMd5 = MD5.encrypt(signKey);
        //4、判断签名是否一致
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中“+”转换成了“ ”,因为需要转换回来
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replace(" ","+");
        paramMap.put("logoData",logoData);
        //调用service的方法
        hospitalService.save(paramMap);
        return Result.ok();
    }
}
