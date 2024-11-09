package edu.tbank.hw5.repository.jpa;

import edu.tbank.hw5.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepositoryJpa extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l JOIN FETCH l.events WHERE l.id = :id")
    Optional<Location> findLocationById(Long id);
}