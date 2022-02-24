package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.service.OfficeService;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.List;

@GraphQLApi
public class OfficeOperations {

    @Inject
    public OfficeService service;

    @Query("office")
    @Description("Get Office by id")
    public Office getOffice(@Name("id") String id) {
        return service.getEntityById(id);
    }

    @Query("allOffices")
    @Description("Get all Offices")
    public List<Office> getAllOffices() {
        return service.getAllEntities();
    }

    @Mutation
    public Office createOffice(Office Office) {
        service.create(Office);
        return Office;
    }

    @Mutation
    public Office updateOffice(Office Office) {
        service.update(Office);
        return Office;
    }

    @Mutation
    public Office deleteOffice(String id) {
        Office Office = service.getEntityById(id);
        service.deleteById(id);
        return Office; //TODO Do we need to return something here?
    }
}
