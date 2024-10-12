package edu.tbank.hw5.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Event implements Serializable {
    Long id;
    String title;
    String price;
    boolean isFree;
}
