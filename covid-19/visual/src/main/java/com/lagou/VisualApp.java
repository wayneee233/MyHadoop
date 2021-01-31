package com.lagou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/31 23:22
 */
@SpringBootApplication
public class VisualApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(VisualApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VisualApp.class);
    }
}
