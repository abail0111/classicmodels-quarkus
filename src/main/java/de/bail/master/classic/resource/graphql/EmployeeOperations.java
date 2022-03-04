package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.service.EmployeeService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Query("allEmployees")
    @Description("Get all Employees")
    public List<Employee> getAllEmployees(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllEntitiesPagination(offset, limit);
    }

    public List<Employee> orders(@Source List<Customer> customers) {
        // TODO implement batching
//        // Batching :
//        // load all orders by customer ids
//        List<Integer> customerIDs = customers.stream().map(Customer::getId).collect(Collectors.toList());
//        List<Order> orders = orderService.getAllByCustomer(customerIDs);
//        // map orders to customer list
//        Map<Customer, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, HashMap::new, Collectors.toCollection(ArrayList::new)));
//        List<List<Order>> results = new ArrayList<>();
//        customers.forEach(customer -> results.add(orderMap.get(customer)));
//        return results;
        return null;
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
