package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.service.OrderService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class OrderOperations {

    @Inject
    public OrderService service;

    @Query("order")
    @Description("Get Order by id")
    public Order getOrder(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("allOrders")
    @Description("Get all Orders")
    public List<Order> getAllOrders(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit,
            @Name("status") String status) {
        if (status != null && !status.isEmpty()) {
            return service.filterByStatus(status, offset, limit);
        }
        return service.getAllEntitiesPagination(offset, limit);
    }

    @Mutation
    public Order createOrder(Order Order) {
        service.create(Order);
        return Order;
    }

    @Mutation
    public Order updateOrder(Order Order) {
        service.update(Order);
        return Order;
    }

    @Mutation
    public Order deleteOrder(int id) {
        Order Order = service.getEntityById(id);
        service.deleteById(id);
        return Order; //TODO Do we need to return something here?
    }
}
