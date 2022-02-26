package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.OrderDetailDto;
import de.bail.master.classic.model.enities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface OrderDetailMapper extends GenericMapper<OrderDetail, OrderDetailDto> {

    @Override
    OrderDetailDto toResource(OrderDetail entity);

    @Override
    OrderDetail toEntity(OrderDetailDto entity);
}
