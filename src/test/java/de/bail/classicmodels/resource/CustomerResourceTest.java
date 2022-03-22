package de.bail.classicmodels.resource;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.resource.rest.CustomerResource;
import de.bail.classicmodels.service.PaymentService;
import de.bail.classicmodels.util.CustomNotFoundException;
import de.bail.classicmodels.model.dto.CustomerDto;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.OrderService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(CustomerResource.class)
public class CustomerResourceTest {

    @InjectMock
    CustomerService customerService;

    @InjectMock
    PaymentService paymentService;

    @InjectMock
    OrderService orderService;

    Customer customer;

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
        when(customerService.update(eq(1), any(Customer.class))).thenReturn(customer);
        when(customerService.update(eq(2), any(Customer.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(customerService).deleteById(eq(1));
        doThrow(new CustomNotFoundException()).when(customerService).deleteById(eq(2));
        // mock payment service
        when(paymentService.getAllByCustomer(anyList())).thenReturn((Collections.singletonList(new Payment())));
        // mock order service
        when(orderService.getAllByCustomer(anyList())).thenReturn(Collections.singletonList(new Order()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().post()
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreate_Status_UnsupportedMediaType() {
        given()
                .header("Content-Type", MediaType.TEXT_HTML)
                .body("")
                .when().post()
                .then()
                .statusCode(415);
    }

    @Test
    public void testCreate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/customer/1"));
    }

    @Test
    public void testCreate_DataObject() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().post()
                .then()
                .statusCode(201)
                .body("id", equalTo(customer.getId()))
                .body("firstName", equalTo(customer.getFirstName()))
                .body("lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Read by ID ------------

    @Test
    public void testReadByID_Status_OK() {
        given()
                .when().get("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testReadByID_Status_NotFound() {
        given()
                .when().get("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReadByID_Header() {
        Response response = given()
                .when().get("/1");
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
    }

    @Test
    public void testReadByID_VCard() {
        // request vcard by content type
        Response response = given()
                .header("Content-Type", "text/x-vcard")
                .when().get("/1");
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals("text/x-vcard;charset=iso-8859-1;profile=vcard", response.headers().get("Content-Type").getValue());
    }

    @Test
    public void testReadByID_DataObject() {
        given()
                .when().get("/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(customer.getId()))
                .body("firstName", equalTo(customer.getFirstName()))
                .body("lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Read Details ------------

    @Test
    public void testReadDetails_Status_OK() {
        given()
                .when().get("/1/details")
                .then()
                .statusCode(200);
    }

    @Test
    public void testReadDetails_Status_NotFound() {
        given()
                .when().get("/2/details")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReadDetails_Header() {
        Response response = given()
                .when().get("/1/details");
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
    }

    @Test
    public void testReadDetails_DataObject() {
        given()
                .when().get("/1/details")
                .then()
                .statusCode(200)
                .body("id", equalTo(customer.getId()))
                .body("firstName", equalTo(customer.getFirstName()))
                .body("lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        given()
                .when().get("/")
                .then()
                .statusCode(200);
    }

    @Test
    public void testReadAll_Header() {
        Response response = given()
                .when().get("/");
        // content type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // total count
        Assertions.assertTrue(response.headers().hasHeaderWithName("x-total-count"));
        Assertions.assertEquals("10", response.headers().get("x-total-count").getValue());
    }

    @Test
    public void testReadAll_DataObject() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(customer.getId()))
                .body("[0].firstName", equalTo(customer.getFirstName()))
                .body("[0].lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().put("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().put("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().put("/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/customer/1"));
    }

    @Test
    public void testUpdate_DataObject() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().put("/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(customer.getId()))
                .body("firstName", equalTo(customer.getFirstName()))
                .body("lastName", equalTo(customer.getLastName()));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Status_OK() {
        given()
                .when().delete("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDelete_Status_NotFound() {
        given()
                .when().delete("/2")
                .then()
                .statusCode(404);
    }
}
