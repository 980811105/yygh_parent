package com.huawei.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Leslie
 * @create: 2021-04-25 10:42
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.huawei")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.huawei")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}
