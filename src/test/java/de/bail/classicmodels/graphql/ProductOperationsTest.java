package de.bail.classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.resource.graphql.ProductOperations;
import de.bail.classicmodels.service.ProductService;
import de.bail.classicmodels.util.CustomNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ProductOperationsTest extends StaticGraphQLTest {

    @Inject
    ProductOperations productOperations;

    @InjectMock
    ProductService productService;

    Product product;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating product line object
        ProductLine productLine = new ProductLine();
        productLine.setId("Classic Cars");
        productLine.setTextDescription("Cars cars cars!");
        // instantiating product object
        product = new Product();
        product.setId("1");
        product.setProductName("Porsche 911");
        product.setProductLine(productLine);
        product.setProductScale("1:18");
        product.setProductVendor("Porsche Models");
        product.setQuantityInStock((short) 999);
        product.setBuyPrice(19.99);
        product.setMsrp(45.15);
        // mock product service
        when(productService.getEntityById(eq("1"))).thenReturn(product);
        when(productService.getEntityById(eq("2"))).thenThrow(new CustomNotFoundException());
        when(productService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(product));
        when(productService.getAllEntities()).thenReturn(Collections.singletonList(product));
        when(productService.count()).thenReturn(10);
        when(productService.create(any(Product.class))).thenReturn(product);
        when(productService.update(argThat(new ProductMatcher("1")))).thenReturn(product);
        when(productService.update(argThat(new ProductMatcher("2")))).thenThrow(new CustomNotFoundException());
        doNothing().when(productService).deleteById(eq("1"));
        doThrow(new CustomNotFoundException()).when(productService).deleteById(eq("2"));
    }

    // ------------ Test Product Resolver ------------

    @Test
    public void testResolver_product_orderDetail() {
        // create products
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId(String.valueOf(i));
            products.add(product);
        }
        // create product detail list with order
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId(String.valueOf(i));
            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            orderDetails.add(detail);
        }
        // mock order detail service
        when(productService.getByIDs(anyList())).thenReturn(products);
        // test resolver
        List<List<Product>> ordersResolved = productOperations.product(orderDetails);
        Assertions.assertArrayEquals(new String[]{"0","1","2","3","4","5","6","7","8","9"},
                ordersResolved.stream().map(list -> list.get(0).getId()).toArray(),
                "The list of products should be in the following shape: {{id::1},{id::2},{id::3},...,{id::9}}");
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("queries", "product", variables)
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
        queryGraphQL("queries", "product", variables)
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
        queryGraphQL("queries", "product", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.product.id", equalTo(product.getId()))
                .body("data.product.productName", equalTo(product.getProductName()))
                .body("data.product.productLine.id", equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "products", variables)
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
        queryGraphQL("queries", "products", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.products[0].id", equalTo(product.getId()))
                .body("data.products[0].productName", equalTo(product.getProductName()))
                .body("data.products[0].productLine.id", equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        queryGraphQL("mutations", "createProduct", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        queryGraphQL("mutations", "createProduct", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createProduct.id", equalTo(product.getId()))
                .body("data.createProduct.productName", equalTo(product.getProductName()))
                .body("data.createProduct.productLine.id", equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        queryGraphQL("mutations", "updateProduct", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        product.setId("2");
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        queryGraphQL("mutations", "updateProduct", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        queryGraphQL("mutations", "updateProduct", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updateProduct.id", equalTo(product.getId()))
                .body("data.updateProduct.productName", equalTo(product.getProductName()))
                .body("data.updateProduct.productLine.id", equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        queryGraphQL("mutations", "deleteProduct", variables)
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
        queryGraphQL("mutations", "deleteProduct", variables)
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
        queryGraphQL("mutations", "deleteProduct", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deleteProduct.id", equalTo(product.getId()))
                .body("data.deleteProduct.productName", equalTo(product.getProductName()))
                .body("data.deleteProduct.productLine.id", equalTo(product.getProductLine().getId()));
    }

    /**
     * Employee Matcher
     */
    public static class ProductMatcher implements ArgumentMatcher<Product> {

        private final String expectedId;

        public ProductMatcher(String id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Product product) {
            return product != null && product.getId().equals(expectedId);
        }
    }
}
