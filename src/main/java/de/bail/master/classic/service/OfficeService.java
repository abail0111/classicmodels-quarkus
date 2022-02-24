package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.util.CrudServiceStr;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OfficeService extends CrudServiceStr<Office> {

    protected OfficeService() {
        super(Office.class);
    }

    @Override
    public Office create(Office entity) {
        save(entity);
        return entity;
    }

}
