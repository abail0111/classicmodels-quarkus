package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.OfficeDto;
import de.bail.master.classic.model.enities.Office;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface OfficeMapper extends GenericMapper<Office, OfficeDto> {

    @Override
    OfficeDto toResource(Office entity);

    @Override
    Office toEntity(OfficeDto entity);
}
