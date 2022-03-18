package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.dto.OrderDto;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.resource.rest.PaymentResource;
import de.bail.master.classic.service.PaymentService;
import de.bail.master.classic.util.CustomNotFoundException;
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
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(PaymentResource.class)
public class PaymentResourceTest {

    @InjectMock
    PaymentService paymentService;

    @BeforeEach
    public void setup() {
        // mock payment service
        when(paymentService.getEntityById(eq(new Payment.PaymentId(1,"1")))).thenReturn(new Payment());
        when(paymentService.getEntityById(eq(new Payment.PaymentId(2,"2")))).thenThrow(new CustomNotFoundException());
        when(paymentService.getAllByCustomer(anyList())).thenReturn(Collections.singletonList(new Payment()));
        when(paymentService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Payment()));
        when(paymentService.getAllEntities()).thenReturn(Collections.singletonList(new Payment()));
        when(paymentService.count()).thenReturn(10);
        when(paymentService.getAllByCustomerCount(anyInt())).thenReturn(10);
        when(paymentService.create(any(Payment.class))).thenReturn(new Payment());
        when(paymentService.update(eq(new Payment.PaymentId(1,"1")), any(Payment.class))).thenReturn(new Payment());
        when(paymentService.update(eq(new Payment.PaymentId(2,"2")), any(Payment.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(paymentService).deleteById(eq(new Payment.PaymentId(1,"1")));
        doThrow(new CustomNotFoundException()).when(paymentService).deleteById(eq(new Payment.PaymentId(2,"2")));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new PaymentDto())
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
                .body(new PaymentDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/payment/"));
    }

    @Test
    public void testCreate_DataObject() {
        PaymentDto paymentDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new PaymentDto())
                .when().post()
                .then()
                .statusCode(201)
                .extract().as(PaymentDto.class);
        Assertions.assertNotNull(paymentDto);
    }

    // ------------ Test Read by Customer ID and check number ------------

    @Test
    public void testReadByID_Status_OK() {
        given()
                .when().get("/1/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testReadByID_Status_NotFound() {
        given()
                .when().get("/2/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReadByID_Header() {
        Response response = given()
                .when().get("/1/1");
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
    }

    @Test
    public void testReadByID_DataObject() {
        PaymentDto paymentDto = given()
                .when().get("/1/1")
                .then()
                .statusCode(200)
                .extract().as(PaymentDto.class);
        Assertions.assertNotNull(paymentDto);
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
        OrderDto[] orderDtoList = given()
                .when().get("/")
                .then()
                .statusCode(200)
                .extract().as(OrderDto[].class);
        Assertions.assertNotNull(orderDtoList);
        Assertions.assertEquals(1, orderDtoList.length);
    }

    // ------------ Test Read All by Customer ID ------------

    @Test
    public void testReadAllByCustomer_Status_OK() {
        given()
                .when().get("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testReadAllByCustomer_Header() {
        Response response = given()
                .when().get("/1");
        // content type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // total count
        Assertions.assertTrue(response.headers().hasHeaderWithName("x-total-count"));
        Assertions.assertEquals("10", response.headers().get("x-total-count").getValue());
    }

    @Test
    public void testReadAllByCustomer_DataObject() {
        PaymentDto[] paymentDtoList = given()
                .when().get("/1")
                .then()
                .statusCode(200)
                .extract().as(PaymentDto[].class);
        Assertions.assertNotNull(paymentDtoList);
        Assertions.assertEquals(1, paymentDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new PaymentDto())
                .when().put("/1/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new PaymentDto())
                .when().put("/2/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new PaymentDto())
                .when().put("/1/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/payment/"));
    }

    @Test
    public void testUpdate_DataObject() {
        PaymentDto paymentDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new PaymentDto())
                .when().put("/1/1")
                .then()
                .statusCode(200)
                .extract().as(PaymentDto.class);
        Assertions.assertNotNull(paymentDtoList);
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Status_OK() {
        given()
                .when().delete("/1/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDelete_Status_NotFound() {
        given()
                .when().delete("/2/2")
                .then()
                .statusCode(404);
    }
}
