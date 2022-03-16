package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.enities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface PaymentMapperNoCustomer extends GenericMapper<Payment, PaymentDto> {

    @Mapping(target = "customer", ignore = true)
    @Override
    PaymentDto toResource(Payment entity);

    @Mapping(target = "customerNumber", source = "customer.id")
    @Override
    Payment toEntity(PaymentDto entity);
}
