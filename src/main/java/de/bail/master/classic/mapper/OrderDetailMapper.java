package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.OrderDetailDto;
import de.bail.master.classic.model.enities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface OrderDetailMapper extends GenericMapper<OrderDetail, OrderDetailDto> {

    @Mapping(target = "order", source = "order.id")
    @Mapping(target = "product", source = "product.id")
    @Override
    OrderDetailDto toResource(OrderDetail entity);

    @Mapping(target = "order.id", source = "order")
    @Mapping(target = "product.id", source = "product")
    @Override
    OrderDetail toEntity(OrderDetailDto entity);
}
