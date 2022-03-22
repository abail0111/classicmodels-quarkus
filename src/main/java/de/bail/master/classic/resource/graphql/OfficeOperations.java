package de.bail.master.classic.resource.graphql;

import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.service.OfficeService;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.List;

@Traced
@GraphQLApi
public class OfficeOperations {

    @Inject
    public OfficeService service;

    @Query("office")
    @Description("Get Office by id")
    public Office getOffice(@Name("id") Integer id) {
        return service.getEntityById(id);
    }

    @Query("offices")
    @Description("Get a list of Offices")
    public List<Office> getAllOffices() {
        return service.getAllEntities();
    }

    @Mutation
    public Office createOffice(Office office) {
        service.create(office);
        return office;
    }

    @Mutation
    public Office updateOffice(Office office) {
        service.update(office);
        return office;
    }

    @Mutation
    public Office deleteOffice(Integer id) {
        Office office = service.getEntityById(id);
        service.deleteById(id);
        return office;
    }
}
