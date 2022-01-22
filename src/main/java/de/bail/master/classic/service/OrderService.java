package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderService extends CrudService<Order> {

    protected OrderService() {
        super(Order.class);
    }

    @Override
    public Order create(Order entity) {
        return null;
    }

}
