package edu.tbank.hw5.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfiguration {
    @Value("${app.kuda-go-url}")
    private String kudaGoBaseUrl;

//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(kudaGoBaseUrl));
//
//        return restTemplate;
//    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(kudaGoBaseUrl).build();
    }
}