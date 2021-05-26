package com.huawei.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:02
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.huawei"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.huawei"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}

