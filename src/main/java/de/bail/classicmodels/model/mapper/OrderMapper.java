package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.dto.OrderDto;
import de.bail.classicmodels.model.enities.Order;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface OrderMapper extends GenericMapper<Order, OrderDto> {

    @Override
    OrderDto toResource(Order entity);

    @Override
    Order toEntity(OrderDto entity);
}
