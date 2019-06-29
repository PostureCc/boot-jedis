package com.chan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("com.chan.mapper")
@SpringBootApplication
public class JpaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaTestApplication.class, args);
    }

}
