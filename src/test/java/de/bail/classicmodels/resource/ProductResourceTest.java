package de.bail.classicmodels.resource;

import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.resource.rest.ProductResource;
import de.bail.classicmodels.util.CustomNotFoundException;
import de.bail.classicmodels.model.dto.ProductDto;
import de.bail.classicmodels.service.ProductService;
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
@TestHTTPEndpoint(ProductResource.class)
public class ProductResourceTest {

    @InjectMock
    ProductService productService;

    @BeforeEach
    public void setup() {
        // mock product service
        when(productService.getEntityById(eq("1"))).thenReturn(new Product());
        when(productService.getEntityById(eq("2"))).thenThrow(new CustomNotFoundException());
        when(productService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Product()));
        when(productService.getAllEntities()).thenReturn(Collections.singletonList(new Product()));
        when(productService.count()).thenReturn(10);
        when(productService.create(any(Product.class))).thenReturn(new Product());
        when(productService.update(eq("1"), any(Product.class))).thenReturn(new Product());
        when(productService.update(eq("2"), any(Product.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(productService).deleteById(eq("1"));
        doThrow(new CustomNotFoundException()).when(productService).deleteById(eq("2"));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductDto())
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
                .body(new ProductDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/product/null"));
    }

    @Test
    public void testCreate_DataObject() {
        ProductDto productDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductDto())
                .when().post()
                .then()
                .statusCode(201)
                .extract().as(ProductDto.class);
        Assertions.assertNotNull(productDto);
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
        ProductDto productDto = given()
                .when().get("/1")
                .then()
                .statusCode(200)
                .extract().as(ProductDto.class);
        Assertions.assertNotNull(productDto);
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
        ProductDto[] productDtoList = given()
                .when().get("/")
                .then()
                .statusCode(200)
                .extract().as(ProductDto[].class);
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(1, productDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductDto())
                .when().put("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductDto())
                .when().put("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductDto())
                .when().put("/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/product/null"));
    }

    @Test
    public void testUpdate_DataObject() {
        ProductDto productDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new ProductDto())
                .when().put("/1")
                .then()
                .statusCode(200)
                .extract().as(ProductDto.class);
        Assertions.assertNotNull(productDtoList);
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
