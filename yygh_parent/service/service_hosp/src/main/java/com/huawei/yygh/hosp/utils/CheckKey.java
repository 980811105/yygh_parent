package com.huawei.yygh.hosp.utils;

import com.huawei.yygh.common.helper.HttpRequestHelper;
import com.huawei.yygh.common.utils.MD5;
import com.huawei.yygh.hosp.service.HospitalSetService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-11 21:17
 **/
public class CheckKey {
    public Map<String, Object> getParamMap(HttpServletRequest request){
        return HttpRequestHelper.switchMap(request.getParameterMap());
    }

    public boolean flagCheck(HttpServletRequest request,HospitalSetService hospitalSetService,String hoscode){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hospSign = (String)paramMap.get("sign");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        return hospSign.equals(signKeyMd5);
    }
}
