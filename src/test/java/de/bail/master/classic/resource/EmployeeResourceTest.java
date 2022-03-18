package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.EmployeeDto;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.resource.rest.EmployeeResource;
import de.bail.master.classic.service.EmployeeService;
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
@TestHTTPEndpoint(EmployeeResource.class)
public class EmployeeResourceTest {

    @InjectMock
    EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        // mock employee service
        when(employeeService.getEntityById(eq(1))).thenReturn(new Employee());
        when(employeeService.getEntityById(eq(2))).thenThrow(new CustomNotFoundException());
        when(employeeService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Employee()));
        when(employeeService.getAllEntities()).thenReturn(Collections.singletonList(new Employee()));
        when(employeeService.count()).thenReturn(10);
        when(employeeService.create(any(Employee.class))).thenReturn(new Employee());
        when(employeeService.update(eq(1), any(Employee.class))).thenReturn(new Employee());
        when(employeeService.update(eq(2), any(Employee.class))).thenThrow(new CustomNotFoundException());
        doNothing().when(employeeService).deleteById(eq(1));
        doThrow(new CustomNotFoundException()).when(employeeService).deleteById(eq(2));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_Created() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
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
                .body(new EmployeeDto())
                .when().post();
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/employee/null"));
    }

    @Test
    public void testCreate_DataObject() {
        EmployeeDto employeeDto = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().post()
                .as(EmployeeDto.class);
        Assertions.assertNotNull(employeeDto);
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
        // setup employee object to use VCard correctly
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        when(employeeService.getEntityById(Mockito.eq(1))).thenReturn(employee);
        // request vcard by content type
        Response response = given()
                .header("Content-Type", "text/x-vcard")
                .when().get("/1");
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals("text/x-vcard;charset=iso-8859-1;profile=vcard", response.headers().get("Content-Type").getValue());
    }

    @Test
    public void testReadByID_DataObject() {
        EmployeeDto employeeDto = given()
                .when().get("/1")
                .as(EmployeeDto.class);
        Assertions.assertNotNull(employeeDto);
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
        EmployeeDto[] employeeDtoList = given()
                .when().get("/")
                .as(EmployeeDto[].class);
        Assertions.assertNotNull(employeeDtoList);
        Assertions.assertEquals(1, employeeDtoList.length);
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().put("/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdate_Status_NotFound() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().put("/2")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdate_Header() {
        Response response = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().put("/1");
        // Content Type
        Assertions.assertTrue(response.headers().hasHeaderWithName("Content-Type"));
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.headers().get("Content-Type").getValue());
        // location
        Assertions.assertTrue(response.headers().hasHeaderWithName("Location"));
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/employee/null"));
    }

    @Test
    public void testUpdate_DataObject() {
        EmployeeDto employeeDtoList = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().put("/1")
                .as(EmployeeDto.class);
        Assertions.assertNotNull(employeeDtoList);
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
