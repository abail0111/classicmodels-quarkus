package de.bail.master.classic.service;

import de.bail.master.classic.enities.Payment;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentService extends CrudService<Payment, Integer> {

    protected PaymentService() {
        super(Payment.class);
    }

    @Override
    public Payment create(Payment entity) {
        return null;
    }

}
