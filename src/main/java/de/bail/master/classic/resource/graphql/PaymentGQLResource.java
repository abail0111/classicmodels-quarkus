package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.PaymentService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class PaymentGQLResource {

    @Inject
    public PaymentService service;

    @Query("payment")
    @Description("Get Payment by id")
    public Payment getPayment(@Name("id") int id) {
        return service.getEntityById(id);
    }

    @Query("allPayments")
    @Description("Get all Payments")
    public List<Payment> getAllPayments() {
        return service.getAllEntities();
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
    public Payment deletePayment(int id) {
        Payment Payment = service.getEntityById(id);
        service.deleteById(id);
        return Payment; //TODO Do we need to return something here?
    }
}
