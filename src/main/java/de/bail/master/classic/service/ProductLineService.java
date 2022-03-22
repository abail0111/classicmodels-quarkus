package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.ProductLine;
import de.bail.master.classic.util.CrudService;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;

/**
 * ProductLine Service
 */
@Traced
@ApplicationScoped
public class ProductLineService extends CrudService<ProductLine, String> {

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected ProductLineService() {
        super(ProductLine.class);
    }

    /**
     * Save a new payment to the database.
     * @param productLine Valid payment object
     * @return persisted payment object
     */
    @Override
    public ProductLine create(ProductLine productLine) {
        if (productLine != null) {
            save(productLine);
        }
        return productLine;
    }

}
