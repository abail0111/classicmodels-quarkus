package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.OrderDetail;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * OrderDetail Service
 */
@Traced
@ApplicationScoped
public class OrderDetailService extends CrudService<OrderDetail, OrderDetail.OrderDetailId> {

    @Inject
    OrderService orderService;

    @Inject
    ProductService productService;

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected OrderDetailService() {
        super(OrderDetail.class);
    }

    /**
     * Save a new orderDetail to the database.
     * The order and product must already exist and will not be created.
     * @param orderDetail Valid orderDetail object
     * @return persisted orderDetail object
     */
    @Override
    public OrderDetail create(OrderDetail orderDetail) {
        if (orderDetail != null && orderDetail.getOrder() != null && orderDetail.getProduct() != null && orderDetail.getProduct().getId() != null) {
            if (!orderService.hasEntity(orderDetail.getOrder())) {
                throw orderService.notFoundException(orderDetail.getOrder());
            }
            if (!productService.hasEntity(orderDetail.getProduct().getId())) {
                throw productService.notFoundException(orderDetail.getProduct().getId());
            }
            save(orderDetail);
        }
        return orderDetail;
    }

    /**
     * Filter all order details by order number
     * @param orderNumber Order number
     * @param offset Starting position
     * @param limit The amount of entities
     * @return List of order details
     */
    public List<OrderDetail> getAllByOrder(Integer orderNumber, int offset, int limit) {
        Query query = em.createNamedQuery("OrderDetail.getAllByOrder");
        query.setParameter("orderNumber", orderNumber);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    /**
     * Count records with applied order number filter
     * @param orderNumber Order number
     * @return Amount of records
     */
    public int getAllByOrderCount(Integer orderNumber) {
        return ((Number) em.createNamedQuery("OrderDetail.getAllByOrder.count")
                .setParameter("orderNumber", orderNumber)
                .getSingleResult())
                .intValue();
    }

    /**
     * Get all Order by oder number list
     * @param orderNumbers List of oder numbers
     * @return List of matching order details
     */
    public List<OrderDetail> getAllByOrders(List<Integer> orderNumbers) {
        Query query = em.createNamedQuery("OrderDetail.getAllByOrders");
        query.setParameter("orderNumbers", orderNumbers);
        return query.getResultList();
    }
}
