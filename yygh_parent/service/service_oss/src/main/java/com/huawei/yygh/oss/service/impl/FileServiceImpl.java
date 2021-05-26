package com.huawei.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.huawei.yygh.oss.service.FileService;
import com.huawei.yygh.oss.utils.ConstantOssPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author: Leslie
 * @create: 2021-05-23 15:36
 **/
@Service
public class FileServiceImpl implements FileService {
    //上传文件到阿里云OSS
    @Override
    public String upload(MultipartFile file) {
        String endpoint = ConstantOssPropertiesUtil.EDNPOINT;
        String accessKeyId = ConstantOssPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtil.SECRECT;
        String bucketName = ConstantOssPropertiesUtil.BUCKET;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //上传文件流
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            String dateUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = dateUrl +"/"+ fileName;
            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //上传之后的文件路径
            return "http://"+bucketName+"."+endpoint+"/"+fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
