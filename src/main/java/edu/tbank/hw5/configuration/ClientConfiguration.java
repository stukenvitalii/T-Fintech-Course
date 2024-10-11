package edu.tbank.hw5.configuration;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Slf4j
@Configuration
public class ClientConfiguration {
    @Value("${app.kuda-go-url}")
    private String kudaGoBaseUrl;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .compress(true);

        return WebClient.builder()
                .baseUrl(kudaGoBaseUrl)
                .filter((request, next) -> {
                    log.info("Request: {} {}", request.method(), request.url());
                    return next.exchange(request)
                            .doOnNext(response -> log.info("Response: {}", response.statusCode()))
                            .doOnError(throwable -> log.error("Error during request: {}", throwable.getMessage()));
                })
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
