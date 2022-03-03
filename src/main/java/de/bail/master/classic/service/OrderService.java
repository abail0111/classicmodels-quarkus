package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class OrderService extends CrudService<Order, Integer> {

    protected OrderService() {
        super(Order.class);
    }

    @Override
    public Order create(Order entity) {
        return null;
    }

    public List<Order> filterByStatus(String status, int offset, int limit) {
        Query query = em.createNamedQuery("Order.filterByStatus");
        query.setParameter("status", status);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public Integer countByFilter() {
        return ((Number) em.createNamedQuery("Order.filterByStatus.count")
                .getSingleResult())
                .intValue();
    }

    public List<Order> getAllByCustomer(List<Integer> customerIDs) {
        Query query = em.createNamedQuery("Order.getAllByCustomer");
        query.setParameter("customers", customerIDs);
        return query.getResultList();
    }

}
