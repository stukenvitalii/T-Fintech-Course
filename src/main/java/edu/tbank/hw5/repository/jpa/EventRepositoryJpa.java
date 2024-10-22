package edu.tbank.hw5.repository.jpa;

import edu.tbank.hw5.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepositoryJpa extends JpaRepository<Event, Long> {
}