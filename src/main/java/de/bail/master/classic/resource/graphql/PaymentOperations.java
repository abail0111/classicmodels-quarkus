package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.PaymentService;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Traced
@GraphQLApi
public class PaymentOperations {

    @Inject
    public PaymentService service;

    @Query("payment")
    @Description("Get Payment by id")
    public Payment getPayment(@Name("customerNumber") int customerNumber, @Name("checkNumber") String checkNumber) {
        return service.getEntityById(new Payment.PaymentId(customerNumber, checkNumber));
    }

    @Query("payments")
    @Description("Get a list of Payments")
    public List<Payment> getAllPayments(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit) {
        return service.getAllEntitiesPagination(offset, limit);
    }

    public List<List<Payment>> payments(@Source List<Customer> customers) {
        // Batching payments for customer
        // load all payments by customer ids
        List<Integer> customerIDs = customers.stream().map(Customer::getId).collect(Collectors.toList());
        List<Payment> payments = service.getAllByCustomer(customerIDs);
        // map payments to customer list
        Map<Integer, List<Payment>> paymentMap = payments.stream().collect(Collectors.groupingBy(Payment::getCustomerNumber, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Payment>> results = new ArrayList<>();
        customers.forEach(customer -> results.add(paymentMap.get(customer.getId())));
        // n+1 :
        // List<List<Payment>> payments = new ArrayList<>();
        // for (Customer customer : customers) {
        //     payments.add(paymentService.getAllByCustomer(customer.getId(), 0, limit));
        // }
        return results;
    }

    @Mutation
    public Payment createPayment(Payment payment) {
        service.create(payment);
        return payment;
    }

    @Mutation
    public Payment updatePayment(Payment payment) {
        service.update(payment);
        return payment;
    }

    @Mutation
    public Payment deletePayment(int customerNumber, String checkNumber) {
        Payment payment = service.getEntityById(new Payment.PaymentId(customerNumber, checkNumber));
        service.deleteById(new Payment.PaymentId(customerNumber, checkNumber));
        return payment;
    }
}
