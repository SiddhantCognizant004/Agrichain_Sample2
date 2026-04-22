package com.cts.Registration_Service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced // This allows resolving service names like "AUDIT-COMPLIANCE-SERVICE"
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}