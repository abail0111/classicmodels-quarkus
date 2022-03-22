package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.util.CrudService;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * Order Service
 */
@Traced
@ApplicationScoped
public class OrderService extends CrudService<Order, Integer> {

    @Inject
    CustomerService customerService;

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected OrderService() {
        super(Order.class);
    }

    /**
     * Save a new order to the database.
     * The customer must already exist and will not be created.
     * @param order Valid order object
     * @return persisted order object
     */
    @Override
    public Order create(Order order) {
        if (order != null && order.getCustomer() != null && order.getCustomer().getId() != null) {
            if (!customerService.hasEntity(order.getCustomer().getId())) {
                throw customerService.notFoundException(order.getCustomer().getId());
            }
            save(order);
        }
        return order;
    }

    /**
     * Filter orders by status
     * @param status Status of order
     * @param offset Starting position
     * @param limit The amount of entities
     * @return Filtered list of Orders
     */
    public List<Order> filterByStatus(String status, int offset, int limit) {
        Query query = em.createNamedQuery("Order.filterByStatus");
        query.setParameter("status", status);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    /**
     * Count records with applied status filter
     * @param status Status of order
     * @return Amount of records
     */
    public Integer countByFilter(String status) {
        return ((Number) em.createNamedQuery("Order.filterByStatus.count")
                .setParameter("status", status)
                .getSingleResult())
                .intValue();
    }

    /**
     * Get all order by customer ids
     * @param customerIDs List of customer ids
     * @return List of orders with matching customer ids
     */
    public List<Order> getAllByCustomer(List<Integer> customerIDs) {
        Query query = em.createNamedQuery("Order.getAllByCustomer");
        query.setParameter("customers", customerIDs);
        return query.getResultList();
    }

}
