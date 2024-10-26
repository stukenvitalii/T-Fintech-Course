package edu.tbank.hw5.memento;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class LocationMemento implements Serializable {

    private String slug;
    private String name;
    private LocalDateTime timestamp;

    public LocationMemento(String slug, String name) {
        this.slug = slug;
        this.name = name;
        timestamp = LocalDateTime.now();
    }
}