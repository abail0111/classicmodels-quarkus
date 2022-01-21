package de.bail.master.classic.service;

import de.bail.master.classic.enities.Office;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OfficeService extends CrudService<Office, Integer> {

    protected OfficeService() {
        super(Office.class);
    }

    @Override
    public Office create(Office entity) {
        save(entity);
        return entity;
    }

}
