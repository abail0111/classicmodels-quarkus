package de.bail.master.classic.resource;

import de.bail.master.classic.enities.Product;
import de.bail.master.classic.service.ProductService;
import de.bail.master.classic.util.CrudResource;
import de.bail.master.classic.util.CrudResourceStr;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource extends CrudResourceStr<Product, ProductService, String> {

    public ProductResource() {
        super("/product/");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Product")
    @Override
    public Response create(@Valid Product entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Product")
    @Override
    public Response read(@PathParam("id") String id) {
        return super.read(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Products")
    @Override
    public Response readAll() {
        return super.readAll();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Product")
    @Override
    public Response update(@PathParam("id") String id, @Valid Product entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "delete Product")
    @Override
    public Response delete(@PathParam("id") String id) {
        return super.delete(id);
    }

}
