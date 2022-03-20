package de.bail.master.classic.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.service.EmployeeService;
import de.bail.master.classic.service.OfficeService;
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
public class OfficeOperationsTest extends StaticGraphQLTest {

    @InjectMock
    OfficeService officeService;

    @InjectMock
    EmployeeService employeeService;

    Office office;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating office object
        office = new Office();
        office.setId("1");
        office.setCity("Berlin");
        office.setPhone("+49 12345 112233");
        office.setAddressLine1("997 Classic Street");
        office.setCountry("Germany");
        office.setPostalCode("10115");
        office.setTerritory("EMEA");
        // instantiating office object
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Brendon");
        employee.setLastName("Storek");
        employee.setEmail("bendon.storek@classicmodels.com");
        employee.setExtension("x123");
        employee.setJobTitle("sales");
        employee.setOffice(office);
        employee.setReportsTo(employee);
        // mock office service
        when(officeService.getEntityById(eq("1"))).thenReturn(office);
        when(officeService.getEntityById(eq("2"))).thenThrow(new CustomNotFoundException());
        when(officeService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(office));
        when(officeService.getAllEntities()).thenReturn(Collections.singletonList(office));
        when(officeService.count()).thenReturn(10);
        when(officeService.create(any(Office.class))).thenReturn(office);
        when(officeService.update(argThat(new OfficeMatcher("1")))).thenReturn(office);
        when(officeService.update(argThat(new OfficeMatcher("2")))).thenThrow(new CustomNotFoundException());
        doNothing().when(officeService).deleteById(eq("1"));
        doThrow(new CustomNotFoundException()).when(officeService).deleteById(eq("2"));
        // mock employee service
        when(employeeService.getAllByOffice((anyList()))).thenReturn(Collections.singletonList(employee));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("queries", "office", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        queryGraphQL("queries", "office", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testReadByID_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("queries", "office", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.office.id", equalTo(office.getId()))
                .body("data.office.city", equalTo(office.getCity()))
                .body("data.office.country", equalTo(office.getCountry()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "offices", variables)
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
        queryGraphQL("queries", "offices", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.offices[0].id", equalTo(office.getId()))
                .body("data.offices[0].city", equalTo(office.getCity()))
                .body("data.offices[0].country", equalTo(office.getCountry()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        queryGraphQL("mutations", "createOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        queryGraphQL("mutations", "createOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createOffice.id", equalTo(office.getId()))
                .body("data.createOffice.city", equalTo(office.getCity()))
                .body("data.createOffice.country", equalTo(office.getCountry()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        queryGraphQL("mutations", "updateOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        office.setId("2");
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        queryGraphQL("mutations", "updateOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        queryGraphQL("mutations", "updateOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updateOffice.id", equalTo(office.getId()))
                .body("data.updateOffice.city", equalTo(office.getCity()))
                .body("data.updateOffice.country", equalTo(office.getCountry()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("mutations", "deleteOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        queryGraphQL("mutations", "deleteOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testDelete_DataObject() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("mutations", "deleteOffice", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deleteOffice.id", equalTo(office.getId()))
                .body("data.deleteOffice.city", equalTo(office.getCity()))
                .body("data.deleteOffice.country", equalTo(office.getCountry()));
    }

    /**
     * Employee Matcher
     */
    public static class OfficeMatcher implements ArgumentMatcher<Office> {

        private final String expectedId;

        public OfficeMatcher(String id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Office office) {
            return office != null && office.getId().equals(expectedId);
        }
    }
}
