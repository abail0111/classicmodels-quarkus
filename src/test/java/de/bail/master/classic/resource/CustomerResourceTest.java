package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.CustomerDetailDto;
import de.bail.master.classic.model.dto.CustomerDto;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.resource.rest.CustomerResource;
import de.bail.master.classic.service.CustomerService;
import de.bail.master.classic.service.OrderService;
import de.bail.master.classic.service.PaymentService;
import de.bail.master.classic.util.CustomNotFoundException;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static io.restassured.RestAssured.given;
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

    @BeforeEach
    public void setup() {
        // mock customer service
        when(customerService.getEntityById(eq(1))).thenReturn(new Customer());
        when(customerService.getEntityById(eq(2))).thenThrow(new CustomNotFoundException());
        when(customerService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Customer()));
        when(customerService.getAllEntities()).thenReturn(Collections.singletonList(new Customer()));
        when(customerService.count()).thenReturn(10);
        when(customerService.create(any(Customer.class))).thenReturn(new Customer());
        when(customerService.update(eq(1), any(Customer.class))).thenReturn(new Customer());
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
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/customer/null"));
    }

    @Test
    public void testCreate_DataObject() {
        CustomerDto customerDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().post()
                .as(CustomerDto.class);
        Assertions.assertNotNull(customerDto);
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
        // setup customer object to use VCard correctly
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        when(customerService.getEntityById(Mockito.eq(1))).thenReturn(customer);
        // request vcard by content type
        Response response = given()
                .header("Content-Type", "text/x-vcard")
                .when().get("/1");
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals("text/x-vcard;charset=iso-8859-1;profile=vcard", response.headers().get("Content-Type").getValue());
    }

    @Test
    public void testReadByID_DataObject() {
        CustomerDto customerDto = given()
                .when().get("/1")
                .as(CustomerDto.class);
        Assertions.assertNotNull(customerDto);
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
        CustomerDetailDto customerDto = given()
                .when().get("/1/details")
                .as(CustomerDetailDto.class);
        Assertions.assertNotNull(customerDto);
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
        CustomerDto[] customerDtoList = given()
                .when().get("/")
                .as(CustomerDto[].class);
        Assertions.assertNotNull(customerDtoList);
        Assertions.assertEquals(1, customerDtoList.length);
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
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/customer/null"));
    }

    @Test
    public void testUpdate_DataObject() {
        CustomerDto customerDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new CustomerDto())
                .when().put("/1")
                .as(CustomerDto.class);
        Assertions.assertNotNull(customerDtoList);
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
