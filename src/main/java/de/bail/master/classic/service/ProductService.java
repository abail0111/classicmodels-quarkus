package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Product;
import de.bail.master.classic.util.CrudServiceStr;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class ProductService extends CrudServiceStr<Product> {

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

}
