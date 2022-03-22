package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.Office;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;

/**
 * Office Service
 */
@Traced
@ApplicationScoped
public class OfficeService extends CrudService<Office, Integer> {

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected OfficeService() {
        super(Office.class);
    }

    /**
     * Save a new office to the database.
     * @param office Valid office object
     * @return persisted office object
     */
    @Override
    public Office create(Office office) {
        if (office != null) {
            save(office);
        }
        return office;
    }

}
