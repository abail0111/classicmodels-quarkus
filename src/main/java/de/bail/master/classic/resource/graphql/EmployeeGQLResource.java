package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.service.EmployeeService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class EmployeeGQLResource {

    @Inject
    public EmployeeService service;

    @Query("employee")
    @Description("Get Employee by id")
    public Employee getEmployee(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("allEmployees")
    @Description("Get all Employees")
    public List<Employee> getAllEmployees() {
        return service.getAllEntities();
    }

    @Mutation
    public Employee createEmployee(Employee Employee) {
        service.create(Employee);
        return Employee;
    }

    @Mutation
    public Employee updateEmployee(Employee Employee) {
        service.update(Employee);
        return Employee;
    }

    @Mutation
    public Employee deleteEmployee(int id) {
        Employee Employee = service.getEntityById(id);
        service.deleteById(id);
        return Employee; //TODO Do we need to return something here?
    }
}
