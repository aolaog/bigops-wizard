/*
 * Copyright 2018 www.yunweibang.com Inc. All rights reserved.
 */
package com.yunweibang.bigops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BigopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigopsApplication.class, args);
    }

}
