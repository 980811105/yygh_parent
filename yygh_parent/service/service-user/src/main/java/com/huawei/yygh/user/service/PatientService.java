package com.huawei.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.user.Patient;

import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-05-24 09:08
 **/
public interface PatientService extends IService<Patient> {
    List<Patient> findAllUserId(Long userId);

    Patient getPatientById(Long id);
}
