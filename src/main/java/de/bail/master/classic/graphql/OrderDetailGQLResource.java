package de.bail.master.classic.graphql;

import de.bail.master.classic.enities.OrderDetail;
import de.bail.master.classic.service.OrderDetailService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class OrderDetailGQLResource {

    @Inject
    public OrderDetailService service;

    @Query("orderDetail")
    @Description("Get OrderDetail by id")
    public OrderDetail getOrderDetail(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("allOrderDetails")
    @Description("Get all OrderDetails")
    public List<OrderDetail> getAllOrderDetails() {
        return service.getAllEntities();
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
    public OrderDetail deleteOrderDetail(int id) {
        OrderDetail OrderDetail = service.getEntityById(id);
        service.deleteById(id);
        return OrderDetail; //TODO Do we need to return something here?
    }
}
