package de.bail.classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.resource.graphql.CustomerOperations;
import de.bail.classicmodels.service.PaymentService;
import de.bail.classicmodels.util.CustomNotFoundException;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.OrderService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CustomerOperationsTest extends StaticGraphQLTest {

    @Inject
    CustomerOperations customerOperations;

    @InjectMock
    CustomerService customerService;

    @InjectMock
    PaymentService paymentService;

    @InjectMock
    OrderService orderService;

    Customer customer;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating employee object
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Brendon");
        employee.setLastName("Storek");
        employee.setEmail("bendon.storek@classicmodels.com");
        employee.setExtension("x123");
        employee.setJobTitle("sales");
        // instantiating customer object
        customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCustomerName("Test Inc.");
        customer.setAddressLine1("6964 Farewell Avenue");
        customer.setSalesRepEmployee(employee);
        // mock customer service
        when(customerService.getEntityById(eq(1))).thenReturn(customer);
        when(customerService.getEntityById(eq(2))).thenThrow(new CustomNotFoundException());
        when(customerService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(customer));
        when(customerService.getAllEntities()).thenReturn(Collections.singletonList(customer));
        when(customerService.count()).thenReturn(10);
        when(customerService.create(any(Customer.class))).thenReturn(customer);
        when(customerService.update(argThat(new CustomerMatcher(1)))).thenReturn(customer);
        when(customerService.update(argThat(new CustomerMatcher(2)))).thenThrow(new CustomNotFoundException());
        doNothing().when(customerService).deleteById(eq(1));
        doThrow(new CustomNotFoundException()).when(customerService).deleteById(eq(2));
        // mock payment service
        when(paymentService.getAllByCustomer(anyList())).thenReturn((Collections.emptyList()));
        // mock order service
        when(orderService.getAllByCustomer(anyList())).thenReturn((Collections.emptyList()));
    }

    // ------------ Test Customer Resolver ------------

    @Test
    public void testResolver_customer_employee() {
        // create employees
        ArrayList<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setId(i);
            employees.add(employee);
        }
        // create customer list with employees
        // in reversed order to check proper mapping
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 9; i >= 0; i--) {
            Employee employee = new Employee();
            employee.setId(i);
            Customer customer = new Customer();
            customer.setId(i);
            customer.setSalesRepEmployee(employee);
            customers.add(customer);
        }
        // mock customer service
        when(customerService.getAllCustomerByEmployees(anyList())).thenReturn(customers);
        // test resolver
        List<List<Customer>> customerResolved = customerOperations.customer(employees);
        Assertions.assertArrayEquals(new int[]{0,1,2,3,4,5,6,7,8,9},
                customerResolved.stream().mapToInt(list -> list.get(0).getId()).toArray(),
                "The list of customers should be in the following shape: {{salesRep::1},{salesRep::2},{salesRep::3},...,{salesRep::9}}");
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("queries", "customer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        queryGraphQL("queries", "customer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testReadByID_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("queries", "customer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.customer.id", equalTo(customer.getId()))
                .body("data.customer.firstName", equalTo(customer.getFirstName()))
                .body("data.customer.lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "customers", variables)
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
        queryGraphQL("queries", "customers", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.customers[0].id", equalTo(customer.getId()))
                .body("data.customers[0].firstName", equalTo(customer.getFirstName()))
                .body("data.customers[0].lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        queryGraphQL("mutations", "createCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        queryGraphQL("mutations", "createCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createCustomer.id", equalTo(customer.getId()))
                .body("data.createCustomer.firstName", equalTo(customer.getFirstName()))
                .body("data.createCustomer.lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        queryGraphQL("mutations", "updateCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        customer.setId(2);
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        queryGraphQL("mutations", "updateCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        queryGraphQL("mutations", "updateCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updateCustomer.id", equalTo(customer.getId()))
                .body("data.updateCustomer.firstName", equalTo(customer.getFirstName()))
                .body("data.updateCustomer.lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("mutations", "deleteCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        queryGraphQL("mutations", "deleteCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testDelete_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("mutations", "deleteCustomer", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deleteCustomer.id", equalTo(customer.getId()))
                .body("data.deleteCustomer.firstName", equalTo(customer.getFirstName()))
                .body("data.deleteCustomer.lastName", equalTo(customer.getLastName()));
    }

    /**
     * Customer Matcher
     */
    public static class CustomerMatcher implements ArgumentMatcher<Customer> {

        private final Integer expectedId;

        public CustomerMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Customer customer) {
            return customer != null && customer.getId().equals(expectedId);
        }
    }
}
