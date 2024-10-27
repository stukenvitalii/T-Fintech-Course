package edu.tbank.hw5.bootstrap;

import edu.tbank.hw5.bootstrap.command.Command;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tinkoff.measurementloggingstarter.annotation.Timed;

@Timed
@RequiredArgsConstructor
@Component
@Slf4j
public class DataLoader {
    private final Command retrieveCategoriesCommand;
    private final Command retrieveLocationsCommand;

    @PostConstruct
    public void initData() {
        log.info("Starting data loading process");
        retrieveCategoriesCommand.execute();
        retrieveLocationsCommand.execute();
        log.info("Finished data loading process");
    }
}