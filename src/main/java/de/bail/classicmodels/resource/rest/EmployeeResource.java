package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.model.mapper.EmployeeMapper;
import de.bail.classicmodels.model.dto.EmployeeDto;
import de.bail.classicmodels.service.EmployeeService;
import de.bail.classicmodels.util.VCard;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/employee")
public class EmployeeResource extends CrudResource<Employee, EmployeeDto, Integer, EmployeeService, EmployeeMapper> {

    public EmployeeResource() {
        super("/employee/");
    }

    @Override
    public void linkDTO(EmployeeDto dto) {
        if (dto != null && dto.getReportsTo() != null && dto.getReportsTo().getId() != 0) {
            Link link = linkService.BuildLinkRelated("/employee/" + dto.getReportsTo().getId(), MediaType.APPLICATION_JSON);
            dto.getReportsTo().setLink(link);
        }
        if (dto != null && dto.getOffice() != null && dto.getOffice().getId() != null) {
            Link link = linkService.BuildLinkRelated("/office/" + dto.getOffice().getId(), MediaType.APPLICATION_JSON);
            dto.getOffice().setLink(link);
        }
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
        Employee employee = service.getEntityById(id);
        return Response.ok(VCard.createFromEmployee(employee)).build();
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
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Employee")
    @Override
    public Response update(@PathParam("id") Integer id, @Valid EmployeeDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "delete Employee")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }
}
