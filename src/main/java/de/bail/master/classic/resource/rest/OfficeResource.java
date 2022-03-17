package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.OfficeDto;
import de.bail.master.classic.model.enities.Office;
import de.bail.master.classic.service.OfficeService;
import de.bail.master.classic.mapper.OfficeMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/office")
@Produces(MediaType.APPLICATION_JSON)
public class OfficeResource extends CrudResource<Office, OfficeDto, String, OfficeService, OfficeMapper> {

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
    public Response read(@PathParam("id") String id) {
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
    public Response update(@PathParam("id") String id, @Valid OfficeDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "delete Office")
    @Override
    public Response delete(@PathParam("id") String id) {
        return super.delete(id);
    }

}
