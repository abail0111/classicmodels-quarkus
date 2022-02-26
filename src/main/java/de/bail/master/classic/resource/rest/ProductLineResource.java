package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.ProductLineDto;
import de.bail.master.classic.model.enities.ProductLine;
import de.bail.master.classic.service.ProductLineService;
import de.bail.master.classic.mapper.ProductLineMapper;
import de.bail.master.classic.util.CrudResourceStr;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/productline")
@Produces(MediaType.APPLICATION_JSON)
public class ProductLineResource extends CrudResourceStr<ProductLine, ProductLineDto, ProductLineService, ProductLineMapper> {

    public ProductLineResource() {
        super("/productline/");
    }

    @Override
    public void linkDTO(ProductLineDto dto) {

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new ProductLine")
    @Override
    public Response create(@Valid ProductLineDto entity) {
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
    public Response update(@PathParam("id") String id, @Valid ProductLineDto entity) {
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
