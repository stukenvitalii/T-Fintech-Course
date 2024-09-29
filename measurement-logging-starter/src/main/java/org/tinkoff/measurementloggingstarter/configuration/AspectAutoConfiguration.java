package org.tinkoff.measurementloggingstarter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.tinkoff.measurementloggingstarter.annotation.TimedAspect;

@Configuration
@EnableAspectJAutoProxy
public class AspectAutoConfiguration {

    @Bean
    public TimedAspect enableTimeMeasurementLoggingAspect() {
        return new TimedAspect();
    }
}