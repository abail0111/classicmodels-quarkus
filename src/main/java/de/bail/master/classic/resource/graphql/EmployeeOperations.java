package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.*;
import de.bail.master.classic.service.EmployeeService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<Employee> employee(@Source List<Customer> customers) {
        // Batching employees for customers
        // load all employees by id
        List<Integer> employeeIDs = customers.stream().map(customer -> customer.getSalesRepEmployee().getId()).collect(Collectors.toList());
        List<Employee> employees = service.getAllEmployees(employeeIDs);
        // map employees to customer id
        Map<Integer, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
        List<Employee> results = new ArrayList<>();
        customers.forEach(customer -> results.add(employeeMap.get(customer.getSalesRepEmployee().getId())));
        return results;
    }

    public List<List<Employee>> employees(@Source List<Office> offices) {
        // Batching employees for offices
        // load all employees by office id
        List<String> officeIDs = offices.stream().map(Office::getId).collect(Collectors.toList());
        List<Employee> employees = service.getAllByOffice(officeIDs);
        // map employees to office
        Map<Office, List<Employee>> employeeMap = employees.stream().collect(Collectors.groupingBy(Employee::getOffice, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Employee>> results = new ArrayList<>();
        offices.forEach(office -> results.add(employeeMap.get(office)));
        return results;
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
