package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.service.CustomerService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class CustomerOperations {

    @Inject
    public CustomerService service;

    @Query("customer")
    @Description("Get Customer by id")
    public Customer getCustomer(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("allCustomers")
    @Description("Get all Customers")
    public List<Customer> getAllCustomers(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllEntitiesPagination(offset, limit);
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
        return customer; //TODO Do we need to return something here?
    }
}
