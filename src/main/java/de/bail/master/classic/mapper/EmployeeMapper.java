package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.EmployeeDto;
import de.bail.master.classic.model.enities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeDto> {

    @Override
    EmployeeDto toResource(Employee entity);

    @Override
    Employee toEntity(EmployeeDto entity);
}
