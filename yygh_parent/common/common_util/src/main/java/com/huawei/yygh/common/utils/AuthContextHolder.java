package com.huawei.yygh.common.utils;

import com.huawei.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Leslie
 * @create: 2021-05-23 18:28
 * @description : 获取当前用户信息工具类
 **/
public class AuthContextHolder {
    //获取当前用户id
    public static Long getUserId(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        return JwtHelper.getUserId(token);
    }
    //获取当前用户名称
    public static String getUserName(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
