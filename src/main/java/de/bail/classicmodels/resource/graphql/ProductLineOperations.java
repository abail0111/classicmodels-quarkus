package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.service.ProductLineService;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.List;

@Traced
@GraphQLApi
public class ProductLineOperations {

    @Inject
    public ProductLineService service;

    @Query("productLine")
    @Description("Get ProductLine by id")
    public ProductLine getProductLine(@Name("id") String id) {
        return service.getEntityById(id);
    }

    @Query("productLines")
    @Description("Get a list of ProductLines")
    public List<ProductLine> getAllProductLines() {
        return service.getAllEntities();
    }

    @Mutation
    public ProductLine createProductLine(ProductLine productLine) {
        service.create(productLine);
        return productLine;
    }

    @Mutation
    public ProductLine updateProductLine(ProductLine productLine) {
        service.update(productLine);
        return productLine;
    }

    @Mutation
    public ProductLine deleteProductLine(String id) {
        ProductLine productLine = service.getEntityById(id);
        service.deleteById(id);
        return productLine;
    }
}
