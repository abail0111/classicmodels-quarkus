package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Product;
import de.bail.master.classic.service.ProductService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class ProductOperations {

    @Inject
    public ProductService service;

    @Query("product")
    @Description("Get Product by id")
    public Product getProduct(@Name("id") String id) {
        return service.getEntityById(id);
    }

    @Query("allProducts")
    @Description("Get all Products")
    public List<Product> getAllProducts() {
        return service.getAllEntities();
    }

    @Mutation
    public Product createProduct(Product Product) {
        service.create(Product);
        return Product;
    }

    @Mutation
    public Product updateProduct(Product Product) {
        service.update(Product);
        return Product;
    }

    @Mutation
    public Product deleteProduct(String id) {
        Product Product = service.getEntityById(id);
        service.deleteById(id);
        return Product; //TODO Do we need to return something here?
    }
}
