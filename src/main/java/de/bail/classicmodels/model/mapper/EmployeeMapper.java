package de.bail.classicmodels.model.mapper;

import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.model.dto.EmployeeDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeDto> {

    @Override
    EmployeeDto toResource(Employee entity);

    @Override
    Employee toEntity(EmployeeDto entity);
}
