package de.bail.master.classic.resource;

import de.bail.master.classic.model.dto.EmployeeDto;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.model.enities.Office;
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

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(EmployeeResource.class)
public class EmployeeResourceTest {

    @InjectMock
    EmployeeService employeeService;

    Employee employee;
    
    @BeforeEach
    public void setup() {
        // instantiating office object
        Office office = new Office();
        office.setId("1");
        office.setCity("Berlin");
        office.setPhone("+49 12345 112233");
        office.setAddressLine1("997 Classic Street");
        office.setCountry("Germany");
        office.setPostalCode("10115");
        office.setTerritory("EMEA");
        // instantiating employee object
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Brendon");
        employee.setLastName("Storek");
        employee.setEmail("bendon.storek@classicmodels.com");
        employee.setExtension("x123");
        employee.setJobTitle("sales");
        employee.setOffice(office);
        employee.setReportsTo(employee);
        // instantiating customer object
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCustomerName("Test Inc.");
        customer.setAddressLine1("6964 Farewell Avenue");
        customer.setSalesRepEmployee(employee);
        // mock employee service
        when(employeeService.getEntityById(eq(1))).thenReturn(employee);
        when(employeeService.getEntityById(eq(2))).thenThrow(new CustomNotFoundException());
        when(employeeService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(employee));
        when(employeeService.getAllEntities()).thenReturn(Collections.singletonList(employee));
        when(employeeService.count()).thenReturn(10);
        when(employeeService.create(any(Employee.class))).thenReturn(employee);
        when(employeeService.update(eq(1), any(Employee.class))).thenReturn(employee);
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
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/employee/1"), "Actual Location: " + response.headers().get("Location").getValue());
    }

    @Test
    public void testCreate_DataObject() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().post()
                .then()
                .statusCode(201)
                .body("id", equalTo(employee.getId()))
                .body("firstName", equalTo(employee.getFirstName()))
                .body("lastName", equalTo(employee.getLastName()));
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
                .body("id", equalTo(employee.getId()))
                .body("firstName", equalTo(employee.getFirstName()))
                .body("lastName", equalTo(employee.getLastName()));
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
                .body("[0].id", equalTo(employee.getId()))
                .body("[0].firstName", equalTo(employee.getFirstName()))
                .body("[0].lastName", equalTo(employee.getLastName()));
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
        Assertions.assertTrue(response.headers().get("Location").getValue().contains("/employee/1"), "Actual Location: " + response.headers().get("Location").getValue());
    }

    @Test
    public void testUpdate_DataObject() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(new EmployeeDto())
                .when().put("/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(employee.getId()))
                .body("firstName", equalTo(employee.getFirstName()))
                .body("lastName", equalTo(employee.getLastName()));
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
