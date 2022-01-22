package de.bail.master.classic.service;

import de.bail.master.classic.model.dto.ProductDto;
import de.bail.master.classic.model.enities.Product;
import de.bail.master.classic.util.CrudServiceStr;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductService extends CrudServiceStr<Product> {

    protected ProductService() {
        super(Product.class);
    }

    @Override
    public Product create(Product entity) {
        return null;
    }

}
