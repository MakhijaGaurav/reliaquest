package com.reliaquest.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
@Setter
public class RestTemplateConfig {

    @Bean("employee-rest-template")
    public RestTemplate employeeRestTemplate(
            @Value("${employee-server.hostname}") String employeeBaseUrl, RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.rootUri(employeeBaseUrl).build();
        return restTemplate;
    }
}
