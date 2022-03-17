package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.OrderDetailDto;
import de.bail.master.classic.model.enities.OrderDetail;
import de.bail.master.classic.service.OrderDetailService;
import de.bail.master.classic.mapper.OrderDetailMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/orderdetail")
@Produces(MediaType.APPLICATION_JSON)
public class OrderDetailResource extends CrudResource<OrderDetail, OrderDetailDto, OrderDetail.OrderDetailId, OrderDetailService, OrderDetailMapper> {

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
    @Path("/{order}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all OrderDetails by Order Number")
    public Response read(@PathParam("order") Integer order,
                         @QueryParam("offset") @DefaultValue("0") int offset,
                         @QueryParam("limit") @DefaultValue("100") int limit) {

        List<OrderDetail> entities = service.getAllByOrder(order, offset, limit);
        List<OrderDetailDto> dto = mapper.toResourceList(entities);
        dto.forEach(this::linkDTO);
        int count = service.getAllByOrderCount(order);
        return Response.ok(dto).header("x-total-count", count).build();
    }

    @GET
    @Path("/{order}/{product}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read OrderDetail")
    public Response read(@PathParam("order") Integer order,
                         @PathParam("product") String product) {
        return super.read(new OrderDetail.OrderDetailId(order, product));
    }

    @PUT
    @Path("/{order}/{product}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update OrderDetail")
    public Response update(@PathParam("order") Integer order,
                           @PathParam("product") String product,
                           @Valid OrderDetailDto entity) {
        return super.update(new OrderDetail.OrderDetailId(order, product), entity);
    }

    @DELETE
    @Path("/{order}/{product}")
    @Operation(summary = "delete OrderDetail")
    public Response delete(@PathParam("order") Integer order,
                           @PathParam("product") String product) {
        return super.delete(new OrderDetail.OrderDetailId(order, product));
    }

}
