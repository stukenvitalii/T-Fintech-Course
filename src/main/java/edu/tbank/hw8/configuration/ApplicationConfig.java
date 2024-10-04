package edu.tbank.hw8.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "currency-app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String bankApiUrl,
        @NotNull Long cacheEvictionInterval
) {}