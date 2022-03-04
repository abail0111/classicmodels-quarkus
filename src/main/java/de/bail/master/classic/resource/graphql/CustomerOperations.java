package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.CustomerService;
import de.bail.master.classic.service.OrderService;
import de.bail.master.classic.service.PaymentService;
import io.smallrye.graphql.api.Context;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<List<Order>> orders(@Source List<Customer> customers) {
        // Batching :
        // load all orders by customer ids
        List<Integer> customerIDs = customers.stream().map(Customer::getId).collect(Collectors.toList());
        List<Order> orders = orderService.getAllByCustomer(customerIDs);
        // map orders to customer list
        Map<Customer, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Order>> results = new ArrayList<>();
        customers.forEach(customer -> results.add(orderMap.get(customer)));
        return results;
    }

    public List<List<Payment>> payments(@Source List<Customer> customers) {
        // Batching :
        // load all orders by customer ids
        List<Integer> customerIDs = customers.stream().map(Customer::getId).collect(Collectors.toList());
        List<Payment> payments = paymentService.getAllByCustomer(customerIDs);
        // map payments to customer list
        Map<Integer, List<Payment>> paymentMap = payments.stream().collect(Collectors.groupingBy(Payment::getCustomerNumber, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Payment>> results = new ArrayList<>();
        customers.forEach(customer -> results.add(paymentMap.get(customer.getId())));
        // n+1 :
        // List<List<Payment>> payments = new ArrayList<>();
        // for (Customer customer : customers) {
        //     payments.add(paymentService.getAllByCustomer(customer.getId(), 0, limit));
        // }
        return results;
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
