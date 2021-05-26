package com.huawei.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.huawei.yygh.common.helper.JwtHelper;
import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.model.user.UserInfo;
import com.huawei.yygh.user.service.UserInfoService;
import com.huawei.yygh.user.utils.ConstantWxPropertiesUtil;
import com.huawei.yygh.user.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-22 20:07
 **/
@Api(tags = "微信操作接口")
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeiXinApiController {
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "获取微信登录参数")
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect(){
        try {
            Map<String, Object> map = new HashMap<>();
            String redirectUri = ConstantWxPropertiesUtil.WX_OPEN_REDIRECT_URL;
            redirectUri = URLEncoder.encode(redirectUri,"UTF-8");
            map.put("appid", ConstantWxPropertiesUtil.WX_OPEN_APP_ID);
            map.put("redirect_uri", redirectUri);
            map.put("scope", "snsapi_login");
            map.put("state", System.currentTimeMillis()+"");//System.currentTimeMillis()+""
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "微信扫描后回调的方法")
    @GetMapping("callback")
    public String callback(String code,String state){
        //1、获取临时票据 code
        System.out.println("code="+code);
        //2、拿到code和微信id和密钥，请求微信固定地址，得到两个值
        //即：使用code、appid、appscrect换取access_token和openid
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtil.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        //使用httpClient请求这个地址
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo="+accessTokenInfo);
            //从返回字符串获取两个值，access_token、openid
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");

            /*
             * 判断数据库中是否存在微信的扫描人信息，根据openid做判断
             */
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openId);
            if (userInfo == null) {//数据库不存在微信消息
                //3、使用access_token、openid请求微信地址，得到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openId);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo="+resultInfo);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息：用户昵称和用户头像
                String nickname = resultUserInfoJson.getString("nickname");
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                //4、获取扫码人信息添加到数据库
                userInfo = new UserInfo();
                userInfo.setOpenid(openId);
                userInfo.setNickName(nickname);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }
            Map<String, String> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            return "redirect:" + ConstantWxPropertiesUtil.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+ "&openid="+map.get("openid")+"&name="+URLEncoder.encode(map.get("name"),"utf-8");
        } catch (Exception exception) {
            exception.printStackTrace();
            return  null;
        }
    }
}
