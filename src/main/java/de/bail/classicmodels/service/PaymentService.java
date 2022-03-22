package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.Payment;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * Payment Service
 */
@Traced
@ApplicationScoped
public class PaymentService extends CrudService<Payment, Payment.PaymentId> {

    @Inject
    CustomerService customerService;

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected PaymentService() {
        super(Payment.class);
    }

    /**
     * Save a new payment to the database.
     * @param payment Valid payment object
     * @return persisted payment object
     */
    @Override
    public Payment create(Payment payment) {
        if (payment != null && payment.getId() != null && payment.getId().getCheckNumber() != null && payment.getId().getCustomerNumber() != null) {
            if (!customerService.hasEntity(payment.getId().getCustomerNumber())) {
                throw customerService.notFoundException(payment.getId().getCustomerNumber());
            }
            save(payment);
        }
        return payment;
    }

    /**
     * Get all payments by customer ids
     * @param customers List of customer ids
     * @return List of matching payments
     */
    public List<Payment> getAllByCustomer(List<Integer> customers) {
        Query query = em.createNamedQuery("Payment.getAllByCustomer");
        query.setParameter("customers", customers);
        return query.getResultList();
    }

    /**
     * Count records with applied customer filter
     * @param customer Customer id
     * @return Amount of records
     */
    public int getAllByCustomerCount(Integer customer) {
        return ((Number) em.createNamedQuery("Payment.getAllByCustomer.count")
                .setParameter("customer", customer)
                .getSingleResult())
                .intValue();
    }

}
