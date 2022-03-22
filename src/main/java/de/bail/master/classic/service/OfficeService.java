package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.util.CrudService;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;

@Traced
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
