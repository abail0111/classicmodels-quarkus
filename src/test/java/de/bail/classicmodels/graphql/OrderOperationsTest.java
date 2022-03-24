package de.bail.classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.*;
import de.bail.classicmodels.resource.graphql.OrderDetailOperations;
import de.bail.classicmodels.resource.graphql.OrderOperations;
import de.bail.classicmodels.util.CustomNotFoundException;
import de.bail.classicmodels.service.OrderDetailService;
import de.bail.classicmodels.service.OrderService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class OrderOperationsTest extends StaticGraphQLTest {

    @Inject
    OrderDetailOperations orderDetailOperations;

    @Inject
    OrderOperations orderOperations;

    @InjectMock
    OrderService orderService;

    @InjectMock
    OrderDetailService orderDetailService;

    Order order;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating order detail object
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNumber(1);
        orderDetail.setProduct(new Product());
        orderDetail.setPriceEach(19.99);
        // instantiating customer object
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCustomerName("Test Inc.");
        customer.setAddressLine1("6964 Farewell Avenue");
        // instantiating order object
        order = new Order();
        order.setId(1);
        order.setStatus("Shipped");
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.of(2022,3,20,0,0, 1));
        // mock order service
        when(orderService.getEntityById(eq(1))).thenReturn(order);
        when(orderService.getEntityById(eq(2))).thenThrow(new CustomNotFoundException());
        when(orderService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(order));
        when(orderService.getAllEntities()).thenReturn(Collections.singletonList(order));
        when(orderService.count()).thenReturn(10);
        when(orderService.create(any(Order.class))).thenReturn(order);
        when(orderService.update(argThat(new OrderMatcher(1)))).thenReturn(order);
        when(orderService.update(argThat(new OrderMatcher(2)))).thenThrow(new CustomNotFoundException());
        doNothing().when(orderService).deleteById(eq(1));
        doThrow(new CustomNotFoundException()).when(orderService).deleteById(eq(2));
        // mock order detail service
        when(orderDetailService.getAllByOrder(anyInt(), anyInt(), anyInt())).thenReturn(Collections.singletonList(orderDetail));
    }

    // ------------ Test Order and OrderDetail Resolver ------------

    @Test
    public void testResolver_order_customer() {
        // create customers
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customers.add(customer);
        }
        // create order list with customer
        // in reversed order to check proper mapping
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 9; i >= 0; i--) {
            Customer customer = new Customer();
            customer.setId(i);
            Order order = new Order();
            order.setId(i);
            order.setCustomer(customer);
            orders.add(order);
        }
        // mock order service
        when(orderService.getAllByCustomer(anyList())).thenReturn(orders);
        // test resolver
        List<List<Order>> ordersResolved = orderOperations.orders(customers);
        Assertions.assertArrayEquals(new int[]{0,1,2,3,4,5,6,7,8,9},
                ordersResolved.stream().mapToInt(list -> list.get(0).getId()).toArray(),
                "The list of orders should be in the following shape: {{customer::1},{customer::2},{customer::3},...,{customer::9}}");
    }

    @Test
    public void testResolver_orderDetail_order() {
        // create orders
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setId(i);
            orders.add(order);
        }
        // create order detail list with order
        // in reversed order to check proper mapping
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 9; i >= 0; i--) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderNumber(i);
            orderDetails.add(detail);
        }
        // mock order detail service
        when(orderDetailService.getAllByOrders(anyList())).thenReturn(orderDetails);
        // test resolver
        List<List<OrderDetail>> ordersResolved = orderDetailOperations.details(orders);
        Assertions.assertArrayEquals(new int[]{0,1,2,3,4,5,6,7,8,9},
                ordersResolved.stream().mapToInt(list -> list.get(0).getOrder()).toArray(),
                "The list of orders should be in the following shape: {{order::1},{order::2},{order::3},...,{order::9}}");
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("queries", "order", variables)
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
        queryGraphQL("queries", "order", variables)
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
        queryGraphQL("queries", "order", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.order.id", equalTo(order.getId()))
                .body("data.order.status", equalTo(order.getStatus()))
                .body("data.order.orderDate", equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        queryGraphQL("queries", "orders", variables)
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
        queryGraphQL("queries", "orders", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.orders[0].id", equalTo(order.getId()))
                .body("data.orders[0].status", equalTo(order.getStatus()))
                .body("data.orders[0].orderDate", equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate_Status_OK() {
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        queryGraphQL("mutations", "createOrder", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testCreate_DataObject() {
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        queryGraphQL("mutations", "createOrder", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.createOrder.id", equalTo(order.getId()))
                .body("data.createOrder.status", equalTo(order.getStatus()))
                .body("data.createOrder.orderDate", equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Status_OK() {
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        queryGraphQL("mutations", "updateOrder", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data", notNullValue())
                .body("$", not(hasKey("errors")));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        order.setId(2);
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        queryGraphQL("mutations", "updateOrder", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("errors", notNullValue())
                .body("errors[0].extensions.code", equalTo("404"));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        queryGraphQL("mutations", "updateOrder", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.updateOrder.id", equalTo(order.getId()))
                .body("data.updateOrder.status", equalTo(order.getStatus()))
                .body("data.updateOrder.orderDate", equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Delete ------------
    @Test
    public void testDelete_Status_OK() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        queryGraphQL("mutations", "deleteOrder", variables)
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
        queryGraphQL("mutations", "deleteOrder", variables)
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
        queryGraphQL("mutations", "deleteOrder", variables)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", not(hasKey("errors")))
                .body("data", notNullValue())
                .body("data.deleteOrder.id", equalTo(order.getId()))
                .body("data.deleteOrder.status", equalTo(order.getStatus()))
                .body("data.deleteOrder.orderDate", equalTo(order.getOrderDate().toString()));
    }

    /**
     * Order Matcher
     */
    public static class OrderMatcher implements ArgumentMatcher<Order> {

        private final Integer expectedId;

        public OrderMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Order order) {
            return order != null && order.getId().equals(expectedId);
        }
    }
}
