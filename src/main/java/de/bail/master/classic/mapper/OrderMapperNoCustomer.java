package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.OrderDto;
import de.bail.master.classic.model.enities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface OrderMapperNoCustomer extends GenericMapper<Order, OrderDto> {

    @Override
    @Mapping(target = "customer", ignore = true)
    OrderDto toResource(Order entity);

    @Override
    Order toEntity(OrderDto entity);
}
