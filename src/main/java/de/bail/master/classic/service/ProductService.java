package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Product;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class ProductService extends CrudService<Product, String> {

    protected ProductService() {
        super(Product.class);
    }

    @Override
    public Product create(Product entity) {
        return null;
    }

    public List<Product> filterByProductLine(String productLine, int offset, int limit) {
        Query query = em.createNamedQuery("Product.filterByProductLine");
        query.setParameter("productLine", productLine);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public Integer countByFilter() {
        return ((Number) em.createNamedQuery("Order.filterByStatus.count")
                .getSingleResult())
                .intValue();
    }

    public List<Product> getByIDs(List<String> products) {
        Query query = em.createNamedQuery("Product.getByIDs");
        query.setParameter("products", products);
        return query.getResultList();
    }

}
