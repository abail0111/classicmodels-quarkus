package de.bail.master.classic.resource.rest;

import de.bail.master.classic.model.dto.EmployeeDto;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.service.EmployeeService;
import de.bail.master.classic.mapper.EmployeeMapper;
import de.bail.master.classic.util.CrudResource;
import de.bail.master.classic.util.VCard;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/employee")
public class EmployeeResource extends CrudResource<Employee, EmployeeDto, EmployeeService, EmployeeMapper> {

    public EmployeeResource() {
        super("/employee/");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Employee")
    @Override
    public Response create(@Valid EmployeeDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Employee")
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
            Employee employee = service.getEntityById(id);
            response = Response.ok(VCard.createFromEmployee(employee)).build();
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
    @Operation(summary = "read all Employees")
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
    @Operation(summary = "update Employee")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid EmployeeDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "delete Employee")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }
}
