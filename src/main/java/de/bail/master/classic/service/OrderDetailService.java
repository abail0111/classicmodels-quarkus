package de.bail.master.classic.service;

import de.bail.master.classic.enities.OrderDetail;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderDetailService extends CrudService<OrderDetail, Integer> {

    protected OrderDetailService() {
        super(OrderDetail.class);
    }

    @Override
    public OrderDetail create(OrderDetail entity) {
        return null;
    }

}
