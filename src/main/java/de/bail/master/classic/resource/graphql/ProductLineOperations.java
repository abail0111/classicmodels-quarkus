package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.ProductLine;
import de.bail.master.classic.service.ProductLineService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class ProductLineOperations {

    @Inject
    public ProductLineService service;

    @Query("productLine")
    @Description("Get ProductLine by id")
    public ProductLine getProductLine(@Name("id") String id) {
        return service.getEntityById(id);
    }

    @Query("allProductLines")
    @Description("Get all ProductLines")
    public List<ProductLine> getAllProductLines() {
        return service.getAllEntities();
    }

    @Mutation
    public ProductLine createProductLine(ProductLine ProductLine) {
        service.create(ProductLine);
        return ProductLine;
    }

    @Mutation
    public ProductLine updateProductLine(ProductLine ProductLine) {
        service.update(ProductLine);
        return ProductLine;
    }

    @Mutation
    public ProductLine deleteProductLine(String id) {
        ProductLine ProductLine = service.getEntityById(id);
        service.deleteById(id);
        return ProductLine; //TODO Do we need to return something here?
    }
}
