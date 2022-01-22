package de.bail.master.classic.mapper;

import de.bail.master.classic.model.dto.EmployeeDto;
import de.bail.master.classic.model.enities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeDto> {

    @Mapping(target = "office", source = "office.id")
    @Mapping(target = "reportsTo", source = "reportsTo.id")
    @Override
    EmployeeDto toResource(Employee entity);

    @Mapping(target = "office.id", source = "office")
    @Mapping(target = "reportsTo.id", source = "reportsTo")
    @Override
    Employee toEntity(EmployeeDto entity);
}
