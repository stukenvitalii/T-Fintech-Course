package edu.tbank.hw5.repository.jpa;

import edu.tbank.hw5.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepositoryJpa extends JpaRepository<Location, Long> {
}