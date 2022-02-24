package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.CustomerDto;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.service.CustomerService;
import de.bail.master.classic.mapper.CustomerMapper;
import de.bail.master.classic.util.CrudResource;
import de.bail.master.classic.util.VCard;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/customer")
public class CustomerResource extends CrudResource<Customer, CustomerDto, CustomerService, CustomerMapper> {

    public CustomerResource() {
        super("/customer/");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Customer")
    @Override
    public Response create(@Valid CustomerDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Customer")
    @Override
    public Response read(@PathParam("id") Integer id) {
        return super.read(id);
    }

    @GET
    @Path("/{id}")
    @Consumes("text/x-vcard")
    @Produces("text/x-vcard; profile=\"vcard\"; charset=iso-8859-1")
    public Response vcard(@PathParam("id") Integer id) {
        Response response;
        try {
            Customer customer = service.getEntityById(id);
            response = Response.ok(VCard.createFromCustomer(customer)).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException | IllegalStateException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Customers")
    @Override
    public Response readAllPagination(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("100") int limit) {
        return super.readAllPagination(offset, limit);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Customer")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid CustomerDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "delete Customer")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }

}
