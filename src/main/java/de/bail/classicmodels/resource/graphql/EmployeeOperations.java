package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.service.EmployeeService;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Traced
@GraphQLApi
public class EmployeeOperations {

    @Inject
    public EmployeeService service;

    @Query("employee")
    @Description("Get Employee by id")
    public Employee getEmployee(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("employees")
    @Description("Get a list of Employees")
    public List<Employee> getAllEmployees(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllEntitiesPagination(offset, limit);
    }

    public List<List<Employee>> employees(@Source List<Office> offices) {
        // Batching employees for offices
        // load all employees by office id
        List<Integer> officeIDs = offices.stream().map(Office::getId).collect(Collectors.toList());
        List<Employee> employees = service.getAllByOffice(officeIDs);
        // map employees to office
        Map<Office, List<Employee>> employeeMap = employees.stream().collect(Collectors.groupingBy(Employee::getOffice, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Employee>> results = new ArrayList<>();
        offices.forEach(office -> results.add(employeeMap.get(office)));
        return results;
    }

    @Mutation
    public Employee createEmployee(Employee employee) {
        service.create(employee);
        return employee;
    }

    @Mutation
    public Employee updateEmployee(Employee employee) {
        service.update(employee);
        return employee;
    }

    @Mutation
    public Employee deleteEmployee(int id) {
        Employee employee = service.getEntityById(id);
        service.deleteById(id);
        return employee;
    }
}
