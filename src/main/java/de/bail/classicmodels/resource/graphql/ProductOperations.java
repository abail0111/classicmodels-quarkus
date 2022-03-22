package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.service.ProductService;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Traced
@GraphQLApi
public class ProductOperations {

    @Inject
    public ProductService service;

    @Query("product")
    @Description("Get Product by id")
    public Product getProduct(@Name("id") String id) {
        return service.getEntityById(id);
    }

    @Query("products")
    @Description("Get a list of Products")
    public List<Product> getAllProducts(
            @Name("offset") @DefaultValue("0") int offset,
            @Name("limit") @DefaultValue("100") int limit,
            @Name("productLine") String productLine) {
        if (productLine != null && !productLine.isEmpty()) {
            return service.filterByProductLine(productLine, offset, limit);
        }
        return service.getAllEntitiesPagination(offset, limit);
    }

    public List<List<Product>> product(@Source List<OrderDetail> orderDetails) {
        // Batching products for order details
        // load all products by orderDetail product ids
        List<String> productIDs = orderDetails.stream().map(orderDetail -> orderDetail.getProduct().getId()).collect(Collectors.toList());
        List<Product> products = service.getByIDs(productIDs);
        // map orders to customer list
        Map<String, List<Product>> orderMap = products.stream().collect(Collectors.groupingBy(Product::getId, HashMap::new, Collectors.toCollection(ArrayList::new)));
        List<List<Product>> results = new ArrayList<>();
        orderDetails.forEach(orderDetail -> results.add(orderMap.get(orderDetail.getProduct().getId())));
        return results;
    }


    @Mutation
    public Product createProduct(Product product) {
        service.create(product);
        return product;
    }

    @Mutation
    public Product updateProduct(Product product) {
        service.update(product);
        return product;
    }

    @Mutation
    public Product deleteProduct(String id) {
        Product product = service.getEntityById(id);
        service.deleteById(id);
        return product;
    }
}
