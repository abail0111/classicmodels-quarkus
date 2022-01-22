package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.ProductLineDto;
import de.bail.master.classic.model.enities.ProductLine;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface ProductLineMapper extends GenericMapper<ProductLine, ProductLineDto> {

    @Override
    ProductLineDto toResource(ProductLine entity);

    @Override
    ProductLine toEntity(ProductLineDto entity);
}
