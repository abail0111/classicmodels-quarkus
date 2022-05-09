package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.dto.ProductDto;
import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.model.mapper.ProductMapper;
import de.bail.classicmodels.service.ProductService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource extends CrudResource<Product, ProductDto, String, ProductService, ProductMapper> {

    public ProductResource() {
        super("/product/");
    }

    @Override
    public void linkDTO(ProductDto dto) {
        if (dto != null && dto.getProductLine() != null && dto.getProductLine().getId() != null) {
            Link link = getLinkService().BuildLinkRelated("/productline/" + dto.getProductLine().getId(), MediaType.APPLICATION_JSON);
            dto.getProductLine().setLink(link);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Product")
    @Override
    public Response create(ProductDto entity) {
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

        List<Product> products;
        int count;
        if (productLine != null && !productLine.isEmpty()) {
            products = getService().filterByProductLine(productLine, offset, limit);
            count = getService().countByFilter(productLine);
        } else {
            products = getService().getAllEntitiesPagination(offset, limit);
            count = getService().count();
        }
        List<ProductDto> dto = getMapper().toResourceList(products);
        dto.forEach(this::linkDTO);
        return Response.ok(dto).header("x-total-count", count).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Product")
    @Override
    public Response update(@PathParam("id") String id, ProductDto entity) {
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
