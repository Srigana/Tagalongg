package com.tagalong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TagAlongApplication {
    public static void main(String[] args) {
        SpringApplication.run(TagAlongApplication.class, args);
    }
}
