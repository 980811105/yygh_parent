package com.huawei.yygh.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Leslie
 * @create: 2021-05-23 13:18
 **/
//取消数据源自动配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.huawei"})
public class ServiceOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class, args);
    }
}

