package edu.tbank.hw5.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Value("${app.kuda-go-url}")
    private String kudaGoBaseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(kudaGoBaseUrl).build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(kudaGoBaseUrl).build();
    }
}