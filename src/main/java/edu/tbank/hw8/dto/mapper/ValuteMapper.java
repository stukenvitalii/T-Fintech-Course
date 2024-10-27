package edu.tbank.hw8.dto.mapper;

import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.entity.Valute;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ValuteMapper {
    Valute toEntity(ValuteDto valuteDto);

    ValuteDto toDto(Valute valute);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    Valute partialUpdate(ValuteDto valuteDto, @MappingTarget Valute valute);
}