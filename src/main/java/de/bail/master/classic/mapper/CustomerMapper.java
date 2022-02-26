package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.CustomerDto;
import de.bail.master.classic.model.enities.Customer;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface CustomerMapper extends GenericMapper<Customer, CustomerDto> {

    @Override
    CustomerDto toResource(Customer entity);

    @Override
    Customer toEntity(CustomerDto entity);
}
