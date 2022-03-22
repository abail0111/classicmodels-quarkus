package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.dto.OfficeDto;
import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.model.mapper.OfficeMapper;
import de.bail.classicmodels.service.OfficeService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/office")
@Produces(MediaType.APPLICATION_JSON)
public class OfficeResource extends CrudResource<Office, OfficeDto, Integer, OfficeService, OfficeMapper> {

    public OfficeResource() {
        super("/office/");
    }

    @Override
    public void linkDTO(OfficeDto dto) {
        // nothing to link
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Office")
    @Override
    public Response create(@Valid OfficeDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Office")
    @Override
    public Response read(@PathParam("id") Integer id) {
        return super.read(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Offices")
    @Override
    public Response readAll() {
        return super.readAll();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Office")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid OfficeDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "delete Office")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }

}
