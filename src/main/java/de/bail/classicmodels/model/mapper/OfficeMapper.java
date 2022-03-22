package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.model.dto.OfficeDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface OfficeMapper extends GenericMapper<Office, OfficeDto> {

    @Override
    OfficeDto toResource(Office entity);

    @Override
    Office toEntity(OfficeDto entity);
}
