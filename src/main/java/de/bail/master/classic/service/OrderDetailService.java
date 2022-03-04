package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class OrderDetailService extends CrudService<OrderDetail, OrderDetail.OrderDetailId> {

    protected OrderDetailService() {
        super(OrderDetail.class);
    }

    @Override
    public OrderDetail create(OrderDetail entity) {
        return null;
    }

    public List<OrderDetail> getAllByOrder(Integer orderNumber, int offset, int limit) {
        Query query = em.createNamedQuery("OrderDetail.getAllByOrder");
        query.setParameter("orderNumber", orderNumber);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public int getAllByOrderCount(Integer orderNumber) {
        return ((Number) em.createNamedQuery("OrderDetail.getAllByOrder.count")
                .setParameter("orderNumber", orderNumber)
                .getSingleResult())
                .intValue();
    }

    public List<OrderDetail> getAllByOrders(List<Integer> orderNumbers) {
        Query query = em.createNamedQuery("OrderDetail.getAllByOrders");
        query.setParameter("orderNumbers", orderNumbers);
        return query.getResultList();
    }
}
