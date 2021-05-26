package com.huawei.yygh.msm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Leslie
 * @create: 2021-05-22 09:44
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.huawei"})
public class ServiceMsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMsmApplication.class, args);
    }

}
