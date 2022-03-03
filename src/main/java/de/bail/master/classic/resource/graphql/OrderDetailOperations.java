package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.service.OrderDetailService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;

@GraphQLApi
public class OrderDetailOperations {

    @Inject
    public OrderDetailService service;

//    @Query("orderDetail")
//    @Description("Get OrderDetail by id")
//    public OrderDetail getOrderDetail(@Name("id") int id) {
//        return service.getEntityById(id);
//    }

    public OrderDetail details(@Source Order order) {
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
    public OrderDetail deleteOrderDetail(int id) {
        OrderDetail OrderDetail = service.getEntityById(id);
        service.deleteById(id);
        return OrderDetail; //TODO Do we need to return something here?
    }
}
