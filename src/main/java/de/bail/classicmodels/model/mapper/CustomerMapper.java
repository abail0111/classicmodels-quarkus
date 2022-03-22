package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface CustomerMapper extends GenericMapper<Customer, CustomerDto> {

    @Override
    CustomerDto toResource(Customer entity);

    @Override
    Customer toEntity(CustomerDto entity);
}
