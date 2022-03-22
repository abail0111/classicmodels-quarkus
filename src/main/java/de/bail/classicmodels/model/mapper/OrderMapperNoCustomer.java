package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.dto.OrderDto;
import de.bail.classicmodels.model.enities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface OrderMapperNoCustomer extends GenericMapper<Order, OrderDto> {

    @Override
    @Mapping(target = "customer", ignore = true)
    OrderDto toResource(Order entity);

    @Override
    Order toEntity(OrderDto entity);
}
