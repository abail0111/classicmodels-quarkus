package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.enities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface PaymentMapper extends GenericMapper<Payment, PaymentDto> {

    @Mapping(target = "customer", source = "customer.id")
    @Override
    PaymentDto toResource(Payment entity);

    @Mapping(target = "customer.id", source = "customer")
    @Override
    Payment toEntity(PaymentDto entity);
}
