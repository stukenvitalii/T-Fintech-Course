package edu.tbank.hw5.repository.jpa;

import edu.tbank.hw5.entity.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface EventRepositoryJpa extends JpaRepository<Event, Long> {
    List<Event> findAll(Specification<Event> specification);

    static Specification<Event> buildSpecification(String title, String place, LocalDate dateFrom, LocalDate toDate) {
        List<Specification<Event>> specifications = new ArrayList<>();
        if (title != null) {
            specifications.add((Specification<Event>) (event, query, cb) -> cb.equal(event.get("title"), title));
        }
        if (place != null) {
            specifications.add((Specification<Event>) (event, query, cb) -> cb.equal(event.get("location").get("name"), place));
        }
        if (dateFrom != null && toDate != null) {
            specifications.add((Specification<Event>)
                    (event, query, cb) -> cb.between(event.get("date"),
                            Timestamp.valueOf(dateFrom.atStartOfDay()), Timestamp.valueOf(toDate.atStartOfDay())));
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}