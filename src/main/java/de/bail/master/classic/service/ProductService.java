package de.bail.master.classic.service;

import de.bail.master.classic.enities.Product;
import de.bail.master.classic.util.CrudServiceStr;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductService extends CrudServiceStr<Product, String> {

    protected ProductService() {
        super(Product.class);
    }

    @Override
    public Product create(Product entity) {
        return null;
    }

}
