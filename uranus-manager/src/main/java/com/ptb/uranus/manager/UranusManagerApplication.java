package com.ptb.uranus.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootApplication
@ComponentScan
public class UranusManagerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UranusManagerApplication.class);
        app.run(args);
    }	
}
