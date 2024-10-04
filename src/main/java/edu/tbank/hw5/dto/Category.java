package edu.tbank.hw5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category implements Serializable {
    private Long id;
    private String slug;
    private String name;
}
