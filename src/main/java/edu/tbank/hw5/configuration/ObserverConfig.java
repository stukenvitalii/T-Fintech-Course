package edu.tbank.hw5.configuration;

import edu.tbank.hw5.observer.CategoryDataObserver;
import edu.tbank.hw5.observer.LocationDataObserver;
import edu.tbank.hw5.repository.CategoryRepository;
import edu.tbank.hw5.repository.LocationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {

    @Bean
    public CategoryDataObserver categoryDataObserver(CategoryRepository categoryRepository) {
        return new CategoryDataObserver(categoryRepository);
    }

    @Bean
    public LocationDataObserver locationDataObserver(LocationRepository locationRepository) {
        return new LocationDataObserver(locationRepository);
    }
}
