package edu.tbank.hw5.bootstrap.command.impl;

import edu.tbank.hw5.bootstrap.command.Command;
import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.observer.DataObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RetrieveLocationsCommand implements Command {
    private final KudaGoClient kudaGoClient;
    private final List<DataObserver<Location>> observers;

    @Override
    public void execute() {
        List<Location> locations = kudaGoClient.getAllLocations();
        if (locations != null) {
            log.info("Fetched {} locations from API", locations.size());
            observers.forEach(observer -> observer.update(locations));
        } else {
            log.warn("No locations fetched from API");
        }
    }
}
