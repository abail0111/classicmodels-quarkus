package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.OrderDetailDto;
import de.bail.master.classic.model.enities.OrderDetail;
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
