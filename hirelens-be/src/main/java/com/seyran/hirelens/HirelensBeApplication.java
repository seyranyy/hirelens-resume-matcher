package com.seyran.hirelens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.seyran.hirelens.entity")
@EnableJpaRepositories(basePackages = "com.seyran.hirelens.repository")
public class HirelensBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HirelensBeApplication.class, args);
    }
}