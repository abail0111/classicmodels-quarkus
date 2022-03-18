package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.OrderDetailDto;
import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.resource.rest.OrderDetailResource;
import de.bail.master.classic.service.OrderDetailService;
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
@TestHTTPEndpoint(OrderDetailResource.class)
public class OrderDetailResourceTest {

    @InjectMock
    OrderDetailService orderDetailService;

    @BeforeEach
    public void setup() {
        // mock orderDetail service
        when(orderDetailService.getEntityById(eq(new OrderDetail.OrderDetailId(1,"1")))).thenReturn(new OrderDetail());
        when(orderDetailService.getEntityById(eq(new OrderDetail.OrderDetailId(2,"2")))).thenThrow(new CustomNotFoundException());
        when(orderDetailService.getAllByOrder(anyInt(), anyInt(), anyInt())).thenReturn(Collections.singletonList(new OrderDetail()));
        when(orderDetailService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new OrderDetail()));
        when(orderDetailService.getAllEntities()).thenReturn(Collections.singletonList(new OrderDetail()));
        when(orderDetailService.count()).thenReturn(10);
        when(orderDetailService.getAllByOrderCount(anyInt())).thenReturn(10);
        when(orderDetailService.create(any(OrderDetail.class))).thenReturn(new OrderDetail());
        when(orderDetailService.update(eq(new OrderDetail.OrderDetailId(1,"1")), any(OrderDetail.class))).thenReturn(new OrderDetail());
        when(orderDetailService.update(eq(new OrderDetail.OrderDetailId(2,"2")), any(OrderDetail.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(orderDetailService).deleteById(eq(new OrderDetail.OrderDetailId(1,"1")));
        doThrow(new CustomNotFoundException()).when(orderDetailService).deleteById(eq(new OrderDetail.OrderDetailId(2,"2")));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDetailDto())
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
                .body(new OrderDetailDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/orderdetail/"));
    }

    @Test
    public void testCreate_DataObject() {
        OrderDetailDto orderDetailDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDetailDto())
                .when().post()
                .as(OrderDetailDto.class);
        Assertions.assertNotNull(orderDetailDto);
    }

    // ------------ Test Read by Order ID and Product ID ------------

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
        OrderDetailDto orderDetailDto = given()
                .when().get("/1/1")
                .as(OrderDetailDto.class);
        Assertions.assertNotNull(orderDetailDto);
    }

    // ------------ Test Read All by Order ID ------------

    @Test
    public void testReadAll_Status_OK() {
        given()
                .when().get("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testReadAll_Header() {
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
    public void testReadAll_DataObject() {
        OrderDetailDto[] orderDetailDtoList = given()
                .when().get("/1")
                .as(OrderDetailDto[].class);
        Assertions.assertNotNull(orderDetailDtoList);
        Assertions.assertEquals(1, orderDetailDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDetailDto())
                .when().put("/1/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDetailDto())
                .when().put("/2/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDetailDto())
                .when().put("/1/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/orderdetail/"));
    }

    @Test
    public void testUpdate_DataObject() {
        OrderDetailDto orderDetailDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OrderDetailDto())
                .when().put("/1/1")
                .as(OrderDetailDto.class);
        Assertions.assertNotNull(orderDetailDtoList);
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
