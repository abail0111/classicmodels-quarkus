package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.LinkService;
import de.bail.master.classic.service.PaymentService;
import de.bail.master.classic.mapper.PaymentMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource extends CrudResource<Payment, PaymentDto, PaymentService, PaymentMapper> {

    @Inject
    LinkService linkService;

    public PaymentResource() {
        super("/payment/");
    }

    @Override
    public void linkDTO(PaymentDto dto) {
        if (dto != null && dto.getCustomer() != null && dto.getCustomer().getId() != 0) {
            Link link = linkService.BuildLinkRelated("/customer/" + dto.getCustomer().getId(), MediaType.APPLICATION_JSON);
            dto.getCustomer().setLink(link);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Payment")
    @Override
    public Response create(@Valid PaymentDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Payment")
    @Override
    public Response read(@PathParam("id") Integer id) {
        return super.read(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Payments")
    @Override
    public Response readAllPagination(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("100") int limit) {
        return super.readAllPagination(offset, limit);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Payment")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid PaymentDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "delete Payment")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }

}
