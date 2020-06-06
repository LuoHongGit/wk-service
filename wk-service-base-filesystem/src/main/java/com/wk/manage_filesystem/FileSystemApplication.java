package com.wk.manage_filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.wk.framework.domain.course")//扫描实体类
@ComponentScan(basePackages={"com.wk.api"})//扫描接口
@ComponentScan(basePackages={"com.wk.manage_filesystem"})
@ComponentScan(basePackages={"com.wk.framework"})//扫描common下的所有类
public class FileSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSystemApplication.class, args);
    }
}
