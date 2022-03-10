package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.service.OrderDetailService;
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
public class OrderDetailOperations {

    @Inject
    public OrderDetailService service;

    public List<OrderDetail> details(@Source Order order, @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllByOrder(order.getId(), 0, limit);
    }

    public List<List<OrderDetail>> details(@Source List<Order> orders) {
        // Batching order details
        // load all order details by order
        List<Integer> orderIDs = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderDetail> orderDetails = service.getAllByOrders(orderIDs);
        // map orderDetails to order id
        Map<Integer, List<OrderDetail>> orderDetailMap = orderDetails.stream().collect(Collectors.groupingBy(OrderDetail::getOrder, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<OrderDetail>> results = new ArrayList<>();
        orders.forEach(order -> results.add(orderDetailMap.get(order.getId())));
        return results;
    }
    
    @Mutation
    public OrderDetail createOrderDetail(OrderDetail OrderDetail) {
        service.create(OrderDetail);
        return OrderDetail;
    }

    @Mutation
    public OrderDetail updateOrderDetail(OrderDetail OrderDetail) {
        service.update(OrderDetail);
        return OrderDetail;
    }

    @Mutation
    public OrderDetail deleteOrderDetail(int order, String product) {
        OrderDetail OrderDetail = service.getEntityById(new OrderDetail.OrderDetailId(order, product));
        service.deleteById(new OrderDetail.OrderDetailId(order, product));
        return OrderDetail; //TODO Do we need to return something here?
    }
}
