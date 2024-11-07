package edu.tbank.hw5.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        @NotNull String kudaGoUrl,
        @NotNull String categoriesEndpoint,
        @NotNull String locationsEndpoint,
        @NotNull String secret
) {}