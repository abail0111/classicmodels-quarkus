package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.ProductDto;
import de.bail.master.classic.model.enities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface ProductMapper extends GenericMapper<Product, ProductDto> {

    @Override
    ProductDto toResource(Product entity);

    @Override
    Product toEntity(ProductDto entity);
}
