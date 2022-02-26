package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.OrderDetailDto;
import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.service.LinkService;
import de.bail.master.classic.service.OrderDetailService;
import de.bail.master.classic.mapper.OrderDetailMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/orderdetail")
@Produces(MediaType.APPLICATION_JSON)
public class OrderDetailResource extends CrudResource<OrderDetail, OrderDetailDto, OrderDetailService, OrderDetailMapper> {

    @Inject
    LinkService linkService;

    public OrderDetailResource() {
        super("/orderdetail/");
    }

    @Override
    public void linkDTO(OrderDetailDto dto) {
        if (dto != null && dto.getOrder() != null && dto.getOrder().getId() != 0) {
            Link link = linkService.BuildLinkRelated("/order/" + dto.getOrder().getId(), MediaType.APPLICATION_JSON);
            dto.getOrder().setLink(link);
        }
        if (dto != null && dto.getProduct() != null && dto.getProduct().getId() != null) {
            Link link = linkService.BuildLinkRelated("/product/" + dto.getProduct().getId(), MediaType.APPLICATION_JSON);
            dto.getProduct().setLink(link);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new OrderDetail")
    @Override
    public Response create(@Valid OrderDetailDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read OrderDetail")
    @Override
    public Response read(@PathParam("id") Integer id) {
        return super.read(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update OrderDetail")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid OrderDetailDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "delete OrderDetail")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }

}
