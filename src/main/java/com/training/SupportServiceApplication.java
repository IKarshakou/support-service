package com.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SupportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupportServiceApplication.class, args);
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplateBuilder().build();
//    }

//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        return new Jackson2ObjectMapperBuilder().build();
//    }
}
