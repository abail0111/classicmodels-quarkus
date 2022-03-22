package de.bail.classicmodels.resource;

import de.bail.classicmodels.model.dto.ProductLineDto;
import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.resource.rest.ProductLineResource;
import de.bail.classicmodels.util.CustomNotFoundException;
import de.bail.classicmodels.service.ProductLineService;
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
@TestHTTPEndpoint(ProductLineResource.class)
public class ProductLineResourceTest {

    @InjectMock
    ProductLineService productLineService;

    @BeforeEach
    public void setup() {
        // mock productLine service
        when(productLineService.getEntityById(eq("1"))).thenReturn(new ProductLine());
        when(productLineService.getEntityById(eq("2"))).thenThrow(new CustomNotFoundException());
        when(productLineService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new ProductLine()));
        when(productLineService.getAllEntities()).thenReturn(Collections.singletonList(new ProductLine()));
        when(productLineService.count()).thenReturn(10);
        when(productLineService.create(any(ProductLine.class))).thenReturn(new ProductLine());
        when(productLineService.update(eq("1"), any(ProductLine.class))).thenReturn(new ProductLine());
        when(productLineService.update(eq("2"), any(ProductLine.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(productLineService).deleteById(eq("1"));
        doThrow(new CustomNotFoundException()).when(productLineService).deleteById(eq("2"));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductLineDto())
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
                .body(new ProductLineDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/productline/null"));
    }

    @Test
    public void testCreate_DataObject() {
        ProductLineDto productLineDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductLineDto())
                .when().post()
                .then()
                .statusCode(201)
                .extract().as(ProductLineDto.class);
        Assertions.assertNotNull(productLineDto);
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
        ProductLineDto productLineDto = given()
                .when().get("/1")
                .then()
                .statusCode(200)
                .extract().as(ProductLineDto.class);
        Assertions.assertNotNull(productLineDto);
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
    }

    @Test
    public void testReadAll_DataObject() {
        ProductLineDto[] productLineDtoList = given()
                .when().get("/")
                .then()
                .statusCode(200)
                .extract().as(ProductLineDto[].class);
        Assertions.assertNotNull(productLineDtoList);
        Assertions.assertEquals(1, productLineDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductLineDto())
                .when().put("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductLineDto())
                .when().put("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductLineDto())
                .when().put("/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/productline/null"));
    }

    @Test
    public void testUpdate_DataObject() {
        ProductLineDto productLineDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductLineDto())
                .when().put("/1")
                .then()
                .statusCode(200)
                .extract().as(ProductLineDto.class);
        Assertions.assertNotNull(productLineDtoList);
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
