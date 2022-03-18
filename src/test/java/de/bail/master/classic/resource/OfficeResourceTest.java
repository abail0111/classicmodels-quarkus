package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.OfficeDto;
import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.resource.rest.OfficeResource;
import de.bail.master.classic.service.OfficeService;
import de.bail.master.classic.util.CustomNotFoundException;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(OfficeResource.class)
public class OfficeResourceTest {

    @InjectMock
    OfficeService officeService;

    @BeforeEach
    public void setup() {
        // mock office service
        when(officeService.getEntityById(eq("1"))).thenReturn(new Office());
        when(officeService.getEntityById(eq("2"))).thenThrow(new CustomNotFoundException());
        when(officeService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Office()));
        when(officeService.getAllEntities()).thenReturn(Collections.singletonList(new Office()));
        when(officeService.count()).thenReturn(10);
        when(officeService.create(any(Office.class))).thenReturn(new Office());
        when(officeService.update(eq("1"), any(Office.class))).thenReturn(new Office());
        when(officeService.update(eq("2"), any(Office.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(officeService).deleteById(eq("1"));
        doThrow(new CustomNotFoundException()).when(officeService).deleteById(eq("2"));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OfficeDto())
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
                .body(new OfficeDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/office/null"));
    }

    @Test
    public void testCreate_DataObject() {
        OfficeDto officeDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OfficeDto())
                .when().post()
                .then()
                .statusCode(201)
                .extract().as(OfficeDto.class);
        Assertions.assertNotNull(officeDto);
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
        OfficeDto officeDto = given()
                .when().get("/1")
                .then()
                .statusCode(200)
                .extract().as(OfficeDto.class);
        Assertions.assertNotNull(officeDto);
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
        OfficeDto[] officeDtoList = given()
                .when().get("/")
                .then()
                .statusCode(200)
                .extract().as(OfficeDto[].class);
        Assertions.assertNotNull(officeDtoList);
        Assertions.assertEquals(1, officeDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OfficeDto())
                .when().put("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OfficeDto())
                .when().put("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OfficeDto())
                .when().put("/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/office/null"));
    }

    @Test
    public void testUpdate_DataObject() {
        OfficeDto officeDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new OfficeDto())
                .when().put("/1")
                .then()
                .statusCode(200)
                .extract().as(OfficeDto.class);
        Assertions.assertNotNull(officeDtoList);
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
