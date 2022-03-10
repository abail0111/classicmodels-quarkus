package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.util.CrudService;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.util.List;

@Traced
@ApplicationScoped
public class PaymentService extends CrudService<Payment, Payment.PaymentId> {

    protected PaymentService() {
        super(Payment.class);
    }

    @Override
    public Payment create(Payment entity) {
        return null;
    }

    public List<Payment> getAllByCustomer(List<Integer> customers) {
        Query query = em.createNamedQuery("Payment.getAllByCustomer");
        query.setParameter("customers", customers);
        return query.getResultList();
    }

    public int getAllByCustomerCount(Integer customer) {
        return ((Number) em.createNamedQuery("Payment.getAllByCustomer.count")
                .setParameter("customer", customer)
                .getSingleResult())
                .intValue();
    }

}
