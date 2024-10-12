package edu.tbank.hw5.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String kudaGoUrl,
        @NotNull String categoriesEndpoint,
        @NotNull String locationsEndpoint,
        @NotNull String eventsEndpoint,
        @NotNull Integer threads,
        @NotNull Duration schedulerPeriod
) {}