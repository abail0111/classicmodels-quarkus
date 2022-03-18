package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.OrderDto;
import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.resource.rest.OrderResource;
import de.bail.master.classic.service.OrderService;
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
@TestHTTPEndpoint(OrderResource.class)
public class OrderResourceTest {

    @InjectMock
    OrderService orderService;

    @BeforeEach
    public void setup() {
        // mock order service
        when(orderService.getEntityById(eq(1))).thenReturn(new Order());
        when(orderService.getEntityById(eq(2))).thenThrow(new CustomNotFoundException());
        when(orderService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Order()));
        when(orderService.getAllEntities()).thenReturn(Collections.singletonList(new Order()));
        when(orderService.count()).thenReturn(10);
        when(orderService.create(any(Order.class))).thenReturn(new Order());
        when(orderService.update(eq(1), any(Order.class))).thenReturn(new Order());
        when(orderService.update(eq(2), any(Order.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(orderService).deleteById(eq(1));
        doThrow(new CustomNotFoundException()).when(orderService).deleteById(eq(2));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDto())
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
                .body(new OrderDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/order/null"));
    }

    @Test
    public void testCreate_DataObject() {
        OrderDto orderDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDto())
                .when().post()
                .as(OrderDto.class);
        Assertions.assertNotNull(orderDto);
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
    public void testReadByID_DataObject() {
        OrderDto orderDto = given()
                .when().get("/1")
                .as(OrderDto.class);
        Assertions.assertNotNull(orderDto);
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
                .as(OrderDto[].class);
        Assertions.assertNotNull(orderDtoList);
        Assertions.assertEquals(1, orderDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDto())
                .when().put("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDto())
                .when().put("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDto())
                .when().put("/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/order/null"));
    }

    @Test
    public void testUpdate_DataObject() {
        OrderDto orderDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDto())
                .when().put("/1")
                .as(OrderDto.class);
        Assertions.assertNotNull(orderDtoList);
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
