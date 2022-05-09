package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.dto.OrderDetailDto;
import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.model.mapper.OrderDetailMapper;
import de.bail.classicmodels.service.OrderDetailService;
import org.eclipse.microprofile.openapi.annotations.Operation;

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
        if (dto != null && dto.getOrder() != null && dto.getOrder().getId() != null && dto.getOrder().getId() != 0) {
            Link link = getLinkService().BuildLinkRelated("/order/" + dto.getOrder().getId(), MediaType.APPLICATION_JSON);
            dto.getOrder().setLink(link);
        }
        if (dto != null && dto.getProduct() != null && dto.getProduct().getId() != null) {
            Link link = getLinkService().BuildLinkRelated("/product/" + dto.getProduct().getId(), MediaType.APPLICATION_JSON);
            dto.getProduct().setLink(link);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new OrderDetail")
    @Override
    public Response create(OrderDetailDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{order}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all OrderDetails by Order Number")
    public Response read(@PathParam("order") Integer order,
                         @QueryParam("offset") @DefaultValue("0") int offset,
                         @QueryParam("limit") @DefaultValue("100") int limit) {

        List<OrderDetail> entities = getService().getAllByOrder(order, offset, limit);
        List<OrderDetailDto> dto = getMapper().toResourceList(entities);
        dto.forEach(this::linkDTO);
        int count = getService().getAllByOrderCount(order);
        return Response.ok(dto).header("x-total-count", count).build();
    }

    @GET
    @Path("/{order}/{product}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read OrderDetail")
    public Response read(@PathParam("order") Integer order,
                         @PathParam("product") String product) {

        OrderDetail.OrderDetailId orderDetailId = new OrderDetail.OrderDetailId(order, product);
        return super.read(orderDetailId);
    }

    @PUT
    @Path("/{order}/{product}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update OrderDetail")
    public Response update(@PathParam("order") Integer order,
                           @PathParam("product") String product,
                           OrderDetailDto entity) {

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
