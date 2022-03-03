package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class PaymentService extends CrudService<Payment, Payment.PaymentId> {

    protected PaymentService() {
        super(Payment.class);
    }

    @Override
    public Payment create(Payment entity) {
        return null;
    }

    public List<Payment> getAllByCustomer(Integer customer, int offset, int limit) {
        Query query = em.createNamedQuery("Payment.getAllByCustomer");
        query.setParameter("customer", customer);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public int getAllByCustomerCount(Integer customer) {
        return ((Number) em.createNamedQuery("Payment.getAllByCustomer.count")
                .setParameter("customer", customer)
                .getSingleResult())
                .intValue();
    }

}
