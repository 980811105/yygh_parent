package com.huawei.yygh.user.client;

import com.huawei.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:30
 **/
@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {
    //获取就诊人
    @GetMapping("/api/user/patient/inner/get/{id}")
    public Patient getPatient(@PathVariable("id") Long id);

}
