package com.huawei.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Leslie
 * @create: 2021-05-21 12:16
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.huawei")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.huawei")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
