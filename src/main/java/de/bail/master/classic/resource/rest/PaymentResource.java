package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.enities.Payment;
import de.bail.master.classic.service.PaymentService;
import de.bail.master.classic.mapper.PaymentMapper;
import de.bail.master.classic.util.CrudResource;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource extends CrudResource<Payment, PaymentDto, Payment.PaymentId, PaymentService, PaymentMapper> {

    public PaymentResource() {
        super("/payment/");
    }

    @Override
    public void linkDTO(PaymentDto dto) {

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
    @Path("/{customerNumber}/{checkNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Payment")
    public Response read(@PathParam("customerNumber") Integer customerNumber,
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
    @Path("/{customerNumber}/{checkNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Payment")
    public Response update(@PathParam("customerNumber") Integer customerNumber,
                           @PathParam("checkNumber") String checkNumber,
                           @Valid PaymentDto entity) {
        return super.update(new Payment.PaymentId(customerNumber, checkNumber), entity);
    }

    @DELETE
    @Path("/{customerNumber}/{checkNumber}")
    @Operation(summary = "delete Payment")
    public Response delete(@PathParam("customerNumber") Integer customerNumber,
                           @PathParam("checkNumber") String checkNumber) {
        return super.delete(new Payment.PaymentId(customerNumber, checkNumber));
    }

}
