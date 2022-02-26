package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.OrderDto;
import de.bail.master.classic.model.dto.ProductDto;
import de.bail.master.classic.model.enities.Order;
import de.bail.master.classic.model.enities.Product;
import de.bail.master.classic.service.LinkService;
import de.bail.master.classic.service.OrderService;
import de.bail.master.classic.mapper.OrderMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource extends CrudResource<Order, OrderDto, OrderService, OrderMapper> {

    @Inject
    LinkService linkService;

    public OrderResource() {
        super("/order/");
    }

    @Override
    public void linkDTO(OrderDto dto) {
        if (dto != null && dto.getCustomer() != null && dto.getCustomer().getId() != 0) {
            Link link = linkService.BuildLinkRelated("/customer/" + dto.getCustomer().getId(), MediaType.APPLICATION_JSON);
            dto.getCustomer().setLink(link);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Order")
    @Override
    public Response create(@Valid OrderDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Order")
    @Override
    public Response read(@PathParam("id") Integer id) {
        return super.read(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Orders")
    public Response readAll(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("100") int limit,
            @QueryParam("status") String status) {
        Response response;
        try {
            List<Order> orders;
            int count;
            if (status != null && !status.isEmpty()) {
                orders = service.filterByStatus(status, offset, limit);
                count = service.countByFilter();
            } else {
                orders = service.getAllEntitiesPagination(offset, limit);
                count = service.count();
            }
            List<OrderDto> dto = mapper.toResourceList(orders);
            dto.forEach(this::linkDTO);
            response = Response.ok(dto)
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
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Order")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid OrderDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "delete Order")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }

}
