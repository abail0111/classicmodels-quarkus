package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.Product;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * Product Service
 */
@Traced
@ApplicationScoped
public class ProductService extends CrudService<Product, String> {

    @Inject
    ProductLineService productLineService;

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected ProductService() {
        super(Product.class);
    }

    /**
     * Save a new product to the database.
     * The product line must already exist and will not be created.
     * @param product Valid product object
     * @return persisted product object
     */
    @Override
    public Product create(Product product) {
        if (product != null && product.getProductLine() != null) {
            if (!productLineService.hasEntity(product.getProductLine().getId())) {
                throw productLineService.notFoundException(product.getProductLine().getId());
            }
            save(product);
        }
        return product;
    }

    /**
     * Filter by product line
     * @param productLine Product line ID
     * @param offset Starting position
     * @param limit The amount of entities
     * @return List of products
     */
    public List<Product> filterByProductLine(String productLine, int offset, int limit) {
        Query query = em.createNamedQuery("Product.filterByProductLine");
        query.setParameter("productLine", productLine);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    /**
     * Count records with applied product line filter
     * @return Amount of records
     */
    public Integer countByFilter(String productLine) {
        return ((Number) em.createNamedQuery("Product.filterByProductLine.count")
                .setParameter("productLine", productLine)
                .getSingleResult())
                .intValue();
    }

    /**
     * Get all products with matching id
     * @param products List of product ids
     * @return List of products
     */
    public List<Product> getByIDs(List<String> products) {
        Query query = em.createNamedQuery("Product.getByIDs");
        query.setParameter("products", products);
        return query.getResultList();
    }

}
