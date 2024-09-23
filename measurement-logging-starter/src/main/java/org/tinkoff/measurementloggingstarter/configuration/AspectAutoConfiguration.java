package org.tinkoff.measurementloggingstarter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.tinkoff.measurementloggingstarter.annotation.EnableTimeMeasurementLoggingAspect;

@Configuration
@EnableAspectJAutoProxy
public class AspectAutoConfiguration {

    @Bean
    public EnableTimeMeasurementLoggingAspect enableTimeMeasurementLoggingAspect() {
        return new EnableTimeMeasurementLoggingAspect();
    }
}