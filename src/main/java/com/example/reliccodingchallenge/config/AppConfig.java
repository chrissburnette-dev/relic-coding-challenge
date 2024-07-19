package com.example.reliccodingchallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {
    private static final int MAX_CLIENTS = 5;
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(MAX_CLIENTS);
    }

}
