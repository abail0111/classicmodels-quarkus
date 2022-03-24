package de.bail.classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.resource.graphql.PaymentOperations;
import de.bail.classicmodels.service.PaymentService;
import de.bail.classicmodels.util.CustomNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PaymentOperationsTest extends StaticGraphQLTest {

    @Inject
    PaymentOperations paymentOperations;

    @InjectMock
    PaymentService paymentService;

    Payment payment;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating payment object
        payment = new Payment();
        payment.setCustomerNumber(1);
        payment.setCheckNumber("1");
        payment.setPaymentDate(Timestamp.valueOf(LocalDateTime.of(2022, 3,20, 0, 0, 1)));
        payment.setAmount(19.99);
        // mock payment service
        when(paymentService.getEntityById(eq(new Payment.PaymentId(1,"1")))).thenReturn(payment);
        when(paymentService.getEntityById(eq(new Payment.PaymentId(2,"2")))).thenThrow(new CustomNotFoundException());
        when(paymentService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(payment));
        when(paymentService.getAllEntities()).thenReturn(Collections.singletonList(payment));
        when(paymentService.count()).thenReturn(10);
        when(paymentService.create(any(Payment.class))).thenReturn(payment);
        when(paymentService.update(argThat(new PaymentMatcher(1,"1")))).thenReturn(payment);
        when(paymentService.update(argThat(new PaymentMatcher(2,"2")))).thenThrow(new CustomNotFoundException());
        doNothing().when(paymentService).deleteById(eq(new Payment.PaymentId(1,"1")));
        doThrow(new CustomNotFoundException()).when(paymentService).deleteById(eq(new Payment.PaymentId(2,"2")));
    }

    // ------------ Test Payment Resolver ------------

    @Test
    public void testResolver_payment_customer() {
        // create customers
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customers.add(customer);
        }
        // create payment list with customer
        // in reversed order to check proper mapping
        ArrayList<Payment> payments = new ArrayList<>();
        for (int i = 9; i >= 0; i--) {
            Payment payment = new Payment();
            payment.setCustomerNumber(i);
            payments.add(payment);
        }
        // mock payment service
        when(paymentService.getAllByCustomer(anyList())).thenReturn(payments);
        // test resolver
        List<List<Payment>> paymentsResolved = paymentOperations.payments(customers);
        Assertions.assertArrayEquals(new int[]{0,1,2,3,4,5,6,7,8,9},
                paymentsResolved.stream().mapToInt(list -> list.get(0).getCustomerNumber()).toArray(),
                "The list of payments should be in the following shape: {{customer::1},{customer::2},{customer::3},...,{customer::9}}");
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "1");
        variables.addProperty("customerNumber", 1);
        queryGraphQL("queries", "payment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "2");
        variables.addProperty("customerNumber", 2);
        queryGraphQL("queries", "payment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testReadByID_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "1");
        variables.addProperty("customerNumber", 1);
        queryGraphQL("queries", "payment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.payment.id.checkNumber", equalTo(payment.getCheckNumber()))
                .body("data.payment.id.customerNumber", equalTo(payment.getCustomerNumber()))
                .body("data.payment.paymentDate", equalTo(payment.getPaymentDate().toLocalDateTime().toString()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "payments", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testReadAll_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "payments", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.payments[0].id.checkNumber", equalTo(payment.getCheckNumber()))
                .body("data.payments[0].id.customerNumber", equalTo(payment.getCustomerNumber()))
                .body("data.payments[0].paymentDate", equalTo(payment.getPaymentDate().toLocalDateTime().toString()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(payment.getId()));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        queryGraphQL("mutations", "createPayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(payment.getId()));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        queryGraphQL("mutations", "createPayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createPayment.id.checkNumber", equalTo(payment.getCheckNumber()))
                .body("data.createPayment.id.customerNumber", equalTo(payment.getCustomerNumber()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(payment.getId()));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        queryGraphQL("mutations", "updatePayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(new Payment.PaymentId(2,"2")));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        queryGraphQL("mutations", "updatePayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(payment.getId()));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        queryGraphQL("mutations", "updatePayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updatePayment.id.checkNumber", equalTo(payment.getCheckNumber()))
                .body("data.updatePayment.id.customerNumber", equalTo(payment.getCustomerNumber()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "1");
        variables.addProperty("customerNumber", 1);
        queryGraphQL("mutations", "deletePayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "2");
        variables.addProperty("customerNumber", 2);
        queryGraphQL("mutations", "deletePayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testDelete_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "1");
        variables.addProperty("customerNumber", 1);
        queryGraphQL("mutations", "deletePayment", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deletePayment.id.checkNumber", equalTo(payment.getCheckNumber()))
                .body("data.deletePayment.id.customerNumber", equalTo(payment.getCustomerNumber()))
                .body("data.deletePayment.paymentDate", equalTo(payment.getPaymentDate().toLocalDateTime().toString()));
    }

    /**
     * Payment Matcher
     */
    public static class PaymentMatcher implements ArgumentMatcher<Payment> {

        private final Integer expectedCustomerNumber;
        private final String expectedCheckNumber;

        public PaymentMatcher(Integer customerNumber, String checkNumber) {
            this.expectedCustomerNumber = customerNumber;
            this.expectedCheckNumber = checkNumber;
        }

        @Override
        public boolean matches(Payment payment) {
            return payment != null
                    && payment.getId().getCustomerNumber().equals(expectedCustomerNumber)
                    && payment.getId().getCheckNumber().equals(expectedCheckNumber);
        }
    }
}
