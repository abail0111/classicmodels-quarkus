package de.bail.master.classic.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.service.*;
import de.bail.master.classic.util.CustomNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class EmployeeOperationsTest extends StaticGraphQLTest {

    @InjectMock
    EmployeeService employeeService;

    @InjectMock
    CustomerService customerService;

    Employee employee;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating office object
        Office office = new Office();
        office.setId(1);
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
        when(employeeService.update(argThat(new EmployeeMatcher(1)))).thenReturn(employee);
        when(employeeService.update(argThat(new EmployeeMatcher(2)))).thenThrow(new CustomNotFoundException());
        doNothing().when(employeeService).deleteById(eq(1));
        doThrow(new CustomNotFoundException()).when(employeeService).deleteById(eq(2));
        // mock customer service
        when(customerService.getAllCustomerByEmployees((anyList()))).thenReturn(Collections.singletonList(customer));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("queries", "employee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        queryGraphQL("queries", "employee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testReadByID_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("queries", "employee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.employee.id", equalTo(employee.getId()))
                .body("data.employee.firstName", equalTo(employee.getFirstName()))
                .body("data.employee.lastName", equalTo(employee.getLastName()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "employees", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testReadAll_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "employees", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.employees[0].id", equalTo(employee.getId()))
                .body("data.employees[0].firstName", equalTo(employee.getFirstName()))
                .body("data.employees[0].lastName", equalTo(employee.getLastName()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        queryGraphQL("mutations", "createEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        queryGraphQL("mutations", "createEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createEmployee.id", equalTo(employee.getId()))
                .body("data.createEmployee.firstName", equalTo(employee.getFirstName()))
                .body("data.createEmployee.lastName", equalTo(employee.getLastName()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        queryGraphQL("mutations", "updateEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        employee.setId(2);
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        queryGraphQL("mutations", "updateEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        queryGraphQL("mutations", "updateEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updateEmployee.id", equalTo(employee.getId()))
                .body("data.updateEmployee.firstName", equalTo(employee.getFirstName()))
                .body("data.updateEmployee.lastName", equalTo(employee.getLastName()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("mutations", "deleteEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        queryGraphQL("mutations", "deleteEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testDelete_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("mutations", "deleteEmployee", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deleteEmployee.id", equalTo(employee.getId()))
                .body("data.deleteEmployee.firstName", equalTo(employee.getFirstName()))
                .body("data.deleteEmployee.lastName", equalTo(employee.getLastName()));
    }

    /**
     * Employee Matcher
     */
    public static class EmployeeMatcher implements ArgumentMatcher<Employee> {

        private final Integer expectedId;

        public EmployeeMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Employee employee) {
            return employee != null && employee.getId().equals(expectedId);
        }
    }
}
