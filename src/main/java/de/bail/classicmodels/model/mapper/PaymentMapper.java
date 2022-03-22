package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.model.dto.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface PaymentMapper extends GenericMapper<Payment, PaymentDto> {

    @Mapping(target = "customer.id", source = "customerNumber")
    @Override
    PaymentDto toResource(Payment entity);

    @Mapping(target = "customerNumber", source = "customer.id")
    @Override
    Payment toEntity(PaymentDto entity);
}
