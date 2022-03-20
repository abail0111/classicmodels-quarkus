package de.bail.master.classic.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.master.classic.model.enities.ProductLine;
import de.bail.master.classic.service.ProductLineService;
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
public class ProductLineOperationsTest extends StaticGraphQLTest {

    @InjectMock
    ProductLineService productLineService;

    ProductLine productLine;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating productLine object
        productLine = new ProductLine();
        productLine.setId("Classic Cars");
        productLine.setTextDescription("Cars cars cars!");
        // mock productLine service
        when(productLineService.getEntityById(eq("1"))).thenReturn(productLine);
        when(productLineService.getEntityById(eq("2"))).thenThrow(new CustomNotFoundException());
        when(productLineService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(productLine));
        when(productLineService.getAllEntities()).thenReturn(Collections.singletonList(productLine));
        when(productLineService.count()).thenReturn(10);
        when(productLineService.create(any(ProductLine.class))).thenReturn(productLine);
        when(productLineService.update(argThat(new ProductLineMatcher("1")))).thenReturn(productLine);
        when(productLineService.update(argThat(new ProductLineMatcher("2")))).thenThrow(new CustomNotFoundException());
        doNothing().when(productLineService).deleteById(eq("1"));
        doThrow(new CustomNotFoundException()).when(productLineService).deleteById(eq("2"));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("queries", "productLine", variables)
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
        queryGraphQL("queries", "productLine", variables)
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
        queryGraphQL("queries", "productLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.productLine.id", equalTo(productLine.getId()))
                .body("data.productLine.textDescription", equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "productLines", variables)
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
        queryGraphQL("queries", "productLines", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.productLines[0].id", equalTo(productLine.getId()))
                .body("data.productLines[0].textDescription", equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        queryGraphQL("mutations", "createProductLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        queryGraphQL("mutations", "createProductLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createProductLine.id", equalTo(productLine.getId()))
                .body("data.createProductLine.textDescription", equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        queryGraphQL("mutations", "updateProductLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        productLine.setId("2");
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        queryGraphQL("mutations", "updateProductLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        queryGraphQL("mutations", "updateProductLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updateProductLine.id", equalTo(productLine.getId()))
                .body("data.updateProductLine.textDescription", equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("mutations", "deleteProductLine", variables)
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
        queryGraphQL("mutations", "deleteProductLine", variables)
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
        queryGraphQL("mutations", "deleteProductLine", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deleteProductLine.id", equalTo(productLine.getId()))
                .body("data.deleteProductLine.textDescription", equalTo(productLine.getTextDescription()));
    }

    /**
     * Employee Matcher
     */
    public static class ProductLineMatcher implements ArgumentMatcher<ProductLine> {

        private final String expectedId;

        public ProductLineMatcher(String id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(ProductLine productLine) {
            return productLine != null && productLine.getId().equals(expectedId);
        }
    }
}
