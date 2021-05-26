package com.huawei.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.user.UserInfo;
import com.huawei.yygh.vo.user.LoginVo;
import com.huawei.yygh.vo.user.UserAuthVo;
import com.huawei.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-21 12:21
 **/
public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> loginUser(LoginVo loginVo);

    UserInfo selectWxInfoOpenId(String openId);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
