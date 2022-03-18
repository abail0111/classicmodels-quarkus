package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.PaymentService;
import de.bail.master.classic.mapper.PaymentMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource extends CrudResource<Payment, PaymentDto, Payment.PaymentId, PaymentService, PaymentMapper> {

    public PaymentResource() {
        super("/payment/");
    }

    @Override
    public void linkDTO(PaymentDto dto) {
        if (dto != null && dto.getCustomer() != null && dto.getCustomer().getId() != null && dto.getCustomer().getId() != 0) {
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
    @Path("/{customer}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Payments by Customer Number")
    public Response read(@PathParam("customer") Integer customerNumber,
                         @QueryParam("offset") @DefaultValue("0") int offset,
                         @QueryParam("limit") @DefaultValue("100") int limit) {

            List<Payment> entities = service.getAllByCustomer(Collections.singletonList(customerNumber));
            List<PaymentDto> dto = mapper.toResourceList(entities);
            dto.forEach(this::linkDTO);
            int count = service.getAllByCustomerCount(customerNumber);
            return Response.ok(dto).header("x-total-count", count).build();
    }

    @GET
    @Path("/{customer}/{checkNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Payment")
    public Response read(@PathParam("customer") Integer customerNumber,
                         @PathParam("checkNumber") String checkNumber) {
        return super.read(new Payment.PaymentId(customerNumber, checkNumber));
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
    @Path("/{customer}/{checkNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Payment")
    public Response update(@PathParam("customer") Integer customerNumber,
                           @PathParam("checkNumber") String checkNumber,
                           @Valid PaymentDto entity) {
        return super.update(new Payment.PaymentId(customerNumber, checkNumber), entity);
    }

    @DELETE
    @Path("/{customer}/{checkNumber}")
    @Operation(summary = "delete Payment")
    public Response delete(@PathParam("customer") Integer customerNumber,
                           @PathParam("checkNumber") String checkNumber) {
        return super.delete(new Payment.PaymentId(customerNumber, checkNumber));
    }

}
