package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.service.OrderService;
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
public class OrderOperations {

    @Inject
    public OrderService service;

    @Query("order")
    @Description("Get Order by id")
    public Order getOrder(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("orders")
    @Description("Get a list of Orders")
    public List<Order> getAllOrders(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit,
            @Name("status") String status) {
        if (status != null && !status.isEmpty()) {
            return service.filterByStatus(status, offset, limit);
        }
        return service.getAllEntitiesPagination(offset, limit);
    }

    public List<List<Order>> orders(@Source List<Customer> customers) {
        // Batching orders for customer
        // load all orders by customer ids
        List<Integer> customerIDs = customers.stream().map(Customer::getId).collect(Collectors.toList());
        List<Order> orders = service.getAllByCustomer(customerIDs);
        // map orders to customer list
        Map<Customer, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Order>> results = new ArrayList<>();
        customers.forEach(customer -> results.add(orderMap.get(customer)));
        return results;
    }

    @Mutation
    public Order createOrder(Order order) {
        service.create(order);
        return order;
    }

    @Mutation
    public Order updateOrder(Order order) {
        service.update(order);
        return order;
    }

    @Mutation
    public Order deleteOrder(int id) {
        Order order = service.getEntityById(id);
        service.deleteById(id);
        return order;
    }
}
