package com.huawei.yygh.user.api;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.common.utils.AuthContextHolder;
import com.huawei.yygh.model.user.Patient;
import com.huawei.yygh.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-05-24 09:07
 **/
@Api(tags = "就诊人管理接口")
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {
    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "获取就诊人列表")
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list =  patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    @ApiOperation(value = "添加就诊人信息")
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient,
                              HttpServletRequest request){
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    @ApiOperation(value = "根据id获取就诊人信息")
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id){
        Patient patient =  patientService.getPatientById(id);
        return Result.ok(patient);
    }

    @ApiOperation(value = "修改就诊人信息")
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient){
        patientService.updateById(patient);
        return Result.ok();
    }

    @ApiOperation(value = "删除就诊人信息")
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id){
        patientService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据就诊人id获取就诊人信息")
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(@PathVariable Long id){
        return patientService.getPatientById(id);
    }
}
