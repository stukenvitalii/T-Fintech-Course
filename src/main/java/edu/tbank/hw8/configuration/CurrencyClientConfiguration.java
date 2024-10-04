package edu.tbank.hw8.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class CurrencyClientConfiguration {
    @Value("${currency-app.bank-api-url}")
    private String bankApiUrl;

    @Bean
    public RestClient bankRestClient(HttpMessageConverter<Object> createXmlHttpMessageConverter) {
        return RestClient
                .builder()
                .baseUrl(bankApiUrl)
                .messageConverters(
                        httpMessageConverters ->
                                httpMessageConverters.add(createXmlHttpMessageConverter))
                .build();
    }
}