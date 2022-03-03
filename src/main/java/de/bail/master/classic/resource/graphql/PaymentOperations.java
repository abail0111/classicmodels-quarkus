package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.PaymentService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class PaymentOperations {

    @Inject
    public PaymentService service;

    @Query("payment")
    @Description("Get Payment by id")
    public Payment getPayment(@Name("customerNumber") int customerNumber, @Name("checkNumber") String checkNumber) {
        return service.getEntityById(new Payment.PaymentId(customerNumber, checkNumber));
    }

    @Query("allPayments")
    @Description("Get all Payments")
    public List<Payment> getAllPayments(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllEntitiesPagination(offset, limit);
    }

    @Mutation
    public Payment createPayment(Payment Payment) {
        service.create(Payment);
        return Payment;
    }

    @Mutation
    public Payment updatePayment(Payment Payment) {
        service.update(Payment);
        return Payment;
    }

    @Mutation
    public Payment deletePayment(int customerNumber, String checkNumber) {
        Payment Payment = service.getEntityById(new Payment.PaymentId(customerNumber, checkNumber));
        service.deleteById(new Payment.PaymentId(customerNumber, checkNumber));
        return Payment; //TODO Do we need to return something here?
    }
}
