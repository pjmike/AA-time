package com.ctg.aatime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ctg.aatime.dao")
public class AatimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AatimeApplication.class, args);
	}
}
