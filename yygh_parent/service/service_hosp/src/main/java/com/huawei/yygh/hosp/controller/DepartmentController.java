package com.huawei.yygh.hosp.controller;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.hosp.service.DepartmentService;
import com.huawei.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-05-14 18:18
 **/
@Api(tags = "科室管理接口")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    @ApiOperation(value = "根据医院编号查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

}
