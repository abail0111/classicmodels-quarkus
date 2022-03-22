package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.dto.ProductLineDto;
import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.model.mapper.ProductLineMapper;
import de.bail.classicmodels.service.ProductLineService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/productline")
@Produces(MediaType.APPLICATION_JSON)
public class ProductLineResource extends CrudResource<ProductLine, ProductLineDto, String, ProductLineService, ProductLineMapper> {

    public ProductLineResource() {
        super("/productline/");
    }

    @Override
    public void linkDTO(ProductLineDto dto) {
        // nothing to link
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
