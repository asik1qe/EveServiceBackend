package com.example.EveSpace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    // Стандартный для запросов
    @Primary
    @Bean(name = "apiExecutor")
    public ExecutorService apiExecutor() {
        return Executors.newFixedThreadPool(6);
    }

    // Уменьшенный для вычислений
    @Bean(name = "smallExecutor")
    public ExecutorService smallExecutor() {
        return Executors.newFixedThreadPool(3);
    }
}
