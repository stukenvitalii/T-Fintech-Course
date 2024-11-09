package edu.tbank.hw5.dto.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.entity.Event;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, componentModel = "spring")
public interface EventMapper {
    Event toEntity(EventDto eventDto);

    EventDto toDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(EventDto eventDto, @MappingTarget Event event);

    Event updateWithNull(EventDto eventDto, @MappingTarget Event event);

    default EventDto fromJsonString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, EventDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to EventDto", e);
        }
    }
}
