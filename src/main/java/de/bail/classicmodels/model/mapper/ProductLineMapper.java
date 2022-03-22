package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.dto.ProductLineDto;
import de.bail.classicmodels.model.enities.ProductLine;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface ProductLineMapper extends GenericMapper<ProductLine, ProductLineDto> {

    @Override
    ProductLineDto toResource(ProductLine entity);

    @Override
    ProductLine toEntity(ProductLineDto entity);
}
