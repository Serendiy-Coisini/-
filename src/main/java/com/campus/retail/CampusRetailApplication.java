package com.campus.retail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园零售销售系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.campus.retail.mapper")
public class CampusRetailApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusRetailApplication.class, args);
        System.out.println("========================================");
        System.out.println("  校园零售销售系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}
