package com.huawei.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Leslie
 * @create: 2021-05-23 15:36
 **/
public interface FileService {
    String upload(MultipartFile file);
}
