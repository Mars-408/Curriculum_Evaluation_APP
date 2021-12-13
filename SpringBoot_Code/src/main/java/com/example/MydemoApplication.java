package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.dao") //定义这个包下面的接口通过动态代理的方式来创建实现类
public class MydemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MydemoApplication.class, args);
    }
}
