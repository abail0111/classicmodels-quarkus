package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.ProductDto;
import de.bail.master.classic.model.enities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface ProductMapper extends GenericMapper<Product, ProductDto> {

    @Mapping(target = "productLine", source = "productLine.id")
    @Override
    ProductDto toResource(Product entity);

    @Mapping(target = "productLine.id", source = "productLine")
    @Override
    Product toEntity(ProductDto entity);
}
