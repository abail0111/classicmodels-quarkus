package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.model.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface ProductMapper extends GenericMapper<Product, ProductDto> {

    @Override
    ProductDto toResource(Product entity);

    @Override
    Product toEntity(ProductDto entity);
}
