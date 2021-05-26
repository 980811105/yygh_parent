package com.huawei.yygh.oss.controller;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author: Leslie
 * @create: 2021-05-23 15:34
 **/
@Api(tags = "阿里云对象存储OSS接口")
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Resource
    private FileService fileService;

    @ApiOperation(value = "上传文件到阿里云OSS")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        String url = fileService.upload(file);
        return Result.ok(url);
    }

}
