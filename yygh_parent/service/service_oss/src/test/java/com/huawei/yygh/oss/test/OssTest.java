package com.huawei.yygh.oss.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

/**
 * @author: Leslie
 * @create: 2021-05-23 14:58
 **/
public class OssTest {
    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI5tR9VP2RtjFeiM92yU7F";
        String accessKeySecret = "ldQibKa1oOVzWMDzTpUKTl2QGTsaBh";
        String bucketName = "lesie-testoss";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建存储空间。
        ossClient.createBucket(bucketName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
