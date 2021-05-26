package com.huawei.yygh.user.api;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.common.utils.AuthContextHolder;
import com.huawei.yygh.model.user.UserInfo;
import com.huawei.yygh.user.service.UserInfoService;
import com.huawei.yygh.vo.user.LoginVo;
import com.huawei.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-21 12:18
 **/
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    @ApiOperation(value = "用户认证接口")
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo  userAuthVo,
                           HttpServletRequest request){
        //传递两个参数，第一个参数用户id，第二个参数认真数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    @ApiOperation(value = "获取用户id信息接口")
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
