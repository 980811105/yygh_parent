package com.huawei.yygh.hosp.controller;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.hosp.service.HospitalService;
import com.huawei.yygh.model.hosp.Hospital;
import com.huawei.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-12 21:41
 **/
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("list/{page}/{limit}")
    public Result findPageHospital(@PathVariable Integer page,
                                   @PathVariable Integer limit,
                                   HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageModel = hospitalService.selectPage(page,limit,hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id,
                               @PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation(value = "查询医院详细信息")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String,Object> hospitalMap = hospitalService.getHospById(id);
        return Result.ok(hospitalMap);
    }
}
