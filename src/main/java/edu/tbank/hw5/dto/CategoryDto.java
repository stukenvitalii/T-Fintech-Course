package edu.tbank.hw5.dto;

import lombok.Value;

/**
 * DTO for {@link edu.tbank.hw5.entity.Category}
 */
@Value
public class CategoryDto {
    String slug;
    String name;
}