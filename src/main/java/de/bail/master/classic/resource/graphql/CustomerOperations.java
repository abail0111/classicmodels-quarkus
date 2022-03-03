package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.CustomerService;
import de.bail.master.classic.service.OrderService;
import de.bail.master.classic.service.PaymentService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@GraphQLApi
public class CustomerOperations {

    @Inject
    public CustomerService customerService;

    @Inject
    OrderService orderService;

    @Inject
    PaymentService paymentService;

    @Query("customer")
    @Description("Get Customer by id")
    public Customer getCustomer(@Name("id") int id) {
        return customerService.getEntityById(id);
    }

    @Query("allCustomers")
    @Description("Get all Customers")
    public List<Customer> getAllCustomers(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return customerService.getAllEntitiesPagination(offset, limit);
    }

    public List<List<Order>> orders(@Source List<Customer> customers, @Name("limit") @DefaultValue("100") int limit) {
        return Collections.singletonList(orderService.getAllEntitiesPagination(0, limit));
    }

    public List<List<Payment>> payments(@Source List<Customer> customers, @Name("limit") @DefaultValue("100") int limit) {
        return Collections.singletonList(paymentService.getAllEntitiesPagination(0, limit));
    }

    @Mutation
    public Customer createCustomer(Customer customer) {
        customerService.create(customer);
        return customer;
    }

    @Mutation
    public Customer updateCustomer(Customer customer) {
        customerService.update(customer);
        return customer;
    }

    @Mutation
    public Customer deleteCustomer(int id) {
        Customer customer = customerService.getEntityById(id);
        customerService.deleteById(id);
        return customer; //TODO Do we need to return something here?
    }
}
