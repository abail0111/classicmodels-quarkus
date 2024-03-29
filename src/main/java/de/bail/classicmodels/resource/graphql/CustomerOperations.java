package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.service.CustomerService;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Traced
@GraphQLApi
public class CustomerOperations {

    @Inject
    public CustomerService service;


    @Query("customer")
    @Description("Get Customer by id")
    public Customer getCustomer(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("customers")
    @Description("Get a list of Customers")
    public List<Customer> getAllCustomers(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllEntitiesPagination(offset, limit);
    }

    public List<List<Customer>> customer(@Source List<Employee> employees) {
        // Batching customer for employees
        // load all customer by employee id
        List<Integer> employeeIDs = employees.stream().map(Employee::getId).collect(Collectors.toList());
        List<Customer> customer = service.getAllCustomerByEmployees(employeeIDs);
        // map employees to customer
        Map<Integer, List<Customer>> customerMap = customer.stream().collect(Collectors.groupingBy(c -> c.getSalesRepEmployee().getId(), HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Customer>> results = new ArrayList<>();
        employees.forEach(employee -> results.add(customerMap.get(employee.getId())));
        return results;
    }

    @Mutation
    public Customer createCustomer(Customer customer) {
        service.create(customer);
        return customer;
    }

    @Mutation
    public Customer updateCustomer(Customer customer) {
        service.update(customer);
        return customer;
    }

    @Mutation
    public Customer deleteCustomer(int id) {
        Customer customer = service.getEntityById(id);
        service.deleteById(id);
        return customer;
    }
}
