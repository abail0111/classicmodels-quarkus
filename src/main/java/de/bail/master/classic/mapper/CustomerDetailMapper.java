package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.CustomerDetailDto;
import de.bail.master.classic.model.enities.Customer;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface CustomerDetailMapper extends GenericMapper<Customer, CustomerDetailDto> {

    @Override
    CustomerDetailDto toResource(Customer entity);

    @Override
    Customer toEntity(CustomerDetailDto entity);
}
