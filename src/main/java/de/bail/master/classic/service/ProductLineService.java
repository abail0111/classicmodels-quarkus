package de.bail.master.classic.service;

import de.bail.master.classic.model.dto.ProductLineDto;
import de.bail.master.classic.model.enities.ProductLine;
import de.bail.master.classic.util.CrudServiceStr;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductLineService extends CrudServiceStr<ProductLine> {

    protected ProductLineService() {
        super(ProductLine.class);
    }

    @Override
    public ProductLine create(ProductLine entity) {
        return null;
    }

}
