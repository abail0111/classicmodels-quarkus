package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.OrderDto;
import de.bail.master.classic.model.enities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface OrderMapper extends GenericMapper<Order, OrderDto> {

    @Mapping(target = "customer", source = "customer.id")
    @Override
    OrderDto toResource(Order entity);

    @Mapping(target = "customer.id", source = "customer")
    @Override
    Order toEntity(OrderDto entity);
}
