package de.bail.master.classic.resource;

import de.bail.master.classic.enities.ProductLine;
import de.bail.master.classic.service.ProductLineService;
import de.bail.master.classic.util.CrudResource;
import de.bail.master.classic.util.CrudResourceStr;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/productline")
@Produces(MediaType.APPLICATION_JSON)
public class ProductLineResource extends CrudResourceStr<ProductLine, ProductLineService, String> {

    public ProductLineResource() {
        super("/productline/");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new ProductLine")
    @Override
    public Response create(@Valid ProductLine entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read ProductLine")
    @Override
    public Response read(@PathParam("id") String id) {
        return super.read(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all ProductLines")
    @Override
    public Response readAll() {
        return super.readAll();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update ProductLine")
    @Override
    public Response update(@PathParam("id") String id, @Valid ProductLine entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "delete ProductLine")
    @Override
    public Response delete(@PathParam("id") String id) {
        return super.delete(id);
    }

}
