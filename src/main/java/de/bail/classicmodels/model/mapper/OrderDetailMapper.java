package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.dto.OrderDetailDto;
import de.bail.classicmodels.model.enities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface OrderDetailMapper extends GenericMapper<OrderDetail, OrderDetailDto> {

    @Mapping(target = "order.id", source = "order")
    @Override
    OrderDetailDto toResource(OrderDetail entity);

    @Mapping(target = "order", source = "order.id")
    @Override
    OrderDetail toEntity(OrderDetailDto entity);
}
