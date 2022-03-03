package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.service.OrderDetailService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class OrderDetailOperations {

    @Inject
    public OrderDetailService service;

    public List<OrderDetail> details(@Source Order order, @Name("limit") @DefaultValue("100") int limit) {
        service.getAllByOrder(order.getId(), 0, limit);
        return null;
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
