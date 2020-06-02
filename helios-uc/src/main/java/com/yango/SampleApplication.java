package com.yango;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan("com.yango.**.mapper")
public class SampleApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

}
