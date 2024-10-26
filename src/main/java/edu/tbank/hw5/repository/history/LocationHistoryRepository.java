package edu.tbank.hw5.repository.history;


import edu.tbank.hw5.memento.LocationMemento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationHistoryRepository {
    void save(String locationSlug, LocationMemento memento);
    List<LocationMemento> findByLocationSlug(String slug);
}