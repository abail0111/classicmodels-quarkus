package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.dto.CustomerDetailDto;
import de.bail.classicmodels.model.enities.Customer;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface CustomerDetailMapper extends GenericMapper<Customer, CustomerDetailDto> {

    @Override
    CustomerDetailDto toResource(Customer entity);

    @Override
    Customer toEntity(CustomerDetailDto entity);
}
