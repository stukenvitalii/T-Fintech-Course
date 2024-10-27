package edu.tbank.hw5.dto;

import edu.tbank.hw5.entity.Event;
import org.mapstruct.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, componentModel = "spring")
public interface EventMapper {
    Event toEntity(EventDto eventDto);

    @Mapping(target = "price", expression = "java(event.isFree() ? 0L : stringToLong(event.getPrice()))")
    EventDto toDto(Event event);

    @Named("stringToLong")
    default Long stringToLong(String price) {
        if (price == null || price.isEmpty()) {
            return 0L;
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(price);
        long minPrice = Long.MAX_VALUE;

        while (matcher.find()) {
            long currentPrice = Long.parseLong(matcher.group());
            if (currentPrice < minPrice) {
                minPrice = currentPrice;
            }
        }

        return minPrice == Long.MAX_VALUE ? 0L : minPrice;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(EventDto eventDto, @MappingTarget Event event);
}