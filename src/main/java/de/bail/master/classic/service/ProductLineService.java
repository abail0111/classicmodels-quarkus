package de.bail.master.classic.service;

import de.bail.master.classic.enities.ProductLine;
import de.bail.master.classic.util.CrudServiceStr;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductLineService extends CrudServiceStr<ProductLine, String> {

    protected ProductLineService() {
        super(ProductLine.class);
    }

    @Override
    public ProductLine create(ProductLine entity) {
        return null;
    }

}
