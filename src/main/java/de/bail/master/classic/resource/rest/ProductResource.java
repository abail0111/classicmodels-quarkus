package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.ProductDto;
import de.bail.master.classic.model.enities.Product;
import de.bail.master.classic.service.ProductService;
import de.bail.master.classic.mapper.ProductMapper;
import de.bail.master.classic.util.CrudResourceStr;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource extends CrudResourceStr<Product, ProductDto, ProductService, ProductMapper> {

    public ProductResource() {
        super("/product/");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Product")
    @Override
    public Response create(@Valid ProductDto entity) {
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
    public Response readAll(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("100") int limit,
            @QueryParam("productLine") String productLine) {
        Response response;
        try {
            List<Product> products;
            int count;
            if (productLine != null && !productLine.isEmpty()) {
                products = service.filterByProductLine(productLine, offset, limit);
                count = service.countByFilter();
            } else {
                products = service.getAllEntitiesPagination(offset, limit);
                count = service.count();
            }
            response = Response.ok(mapper.toResourceList(products))
                    .header("x-total-count", count).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Product")
    @Override
    public Response update(@PathParam("id") String id, @Valid ProductDto entity) {
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
