package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.CustomerDto;
import de.bail.master.classic.model.enities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface CustomerMapper extends GenericMapper<Customer, CustomerDto> {

    @Mapping(target = "salesRepEmployee", source = "salesRepEmployee.id")
    @Override
    CustomerDto toResource(Customer entity);

    @Mapping(target = "salesRepEmployee.id", source = "salesRepEmployee")
    @Override
    Customer toEntity(CustomerDto entity);
}
