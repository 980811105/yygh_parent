package com.huawei.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.model.user.UserInfo;
import com.huawei.yygh.user.service.UserInfoService;
import com.huawei.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-24 10:25
 **/
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "用户列表(条件查询带分页)")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> pageModel =  userInfoService.selectPage(pageParam,userInfoQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "用户锁定")
    @GetMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId, @PathVariable Integer status) {
        userInfoService.lock(userId,status);
        return Result.ok();
    }

    @ApiOperation(value = "用户详情")
    @GetMapping("show/{userId}")
    public Result show(@PathVariable Long userId) {
        Map<String,Object> map =  userInfoService.show(userId);
        return Result.ok(map);
    }

    @ApiOperation(value = "认证审批")
    @GetMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId,
                           @PathVariable Integer authStatus){
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }
}