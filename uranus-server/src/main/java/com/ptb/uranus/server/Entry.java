package com.ptb.uranus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


/**
 * Hello world!
 */
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootApplication
@ComponentScan
@ImportResource({"classpath:applicatoin.xml"})
public class Entry {
    public static void main(String[] args) {
        new SpringApplication(Entry.class).run(args);
    }
}
