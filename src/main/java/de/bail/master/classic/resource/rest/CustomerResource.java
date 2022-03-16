package de.bail.master.classic.resource.rest;

import de.bail.master.classic.mapper.*;
import de.bail.master.classic.model.dto.CustomerDetailDto;
import de.bail.master.classic.model.dto.CustomerDto;
import de.bail.master.classic.model.dto.OrderDto;
import de.bail.master.classic.model.dto.PaymentDto;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.service.CustomerService;
import de.bail.master.classic.service.LinkService;
import de.bail.master.classic.service.OrderService;
import de.bail.master.classic.service.PaymentService;
import de.bail.master.classic.util.CrudResource;
import de.bail.master.classic.util.VCard;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/customer")
public class CustomerResource extends CrudResource<Customer, CustomerDto, Integer, CustomerService, CustomerMapper> {

    @Inject
    LinkService linkService;

    @Inject
    CustomerDetailMapper detailMapper;

    @Inject
    PaymentMapperNoCustomer paymentMapper;

    @Inject
    OrderMapperNoCustomer orderMapper;

    @Inject
    PaymentService paymentService;

    @Inject
    OrderService orderService;

    public CustomerResource() {
        super("/customer/");
    }

    @Override
    @Traced
    public void linkDTO(CustomerDto dto) {
        if (dto != null && dto.getSalesRepEmployee() != null && dto.getSalesRepEmployee().getId() != 0) {
            Link link = linkService.BuildLinkRelated("/employee/" + dto.getSalesRepEmployee().getId(), MediaType.APPLICATION_JSON);
            dto.getSalesRepEmployee().setLink(link);
        }
    }

    @Traced
    public void linkDetailDTO(CustomerDetailDto dto) {
        if (dto != null) {
            if (dto.getSalesRepEmployee() != null && dto.getSalesRepEmployee().getReportsTo() != null && dto.getSalesRepEmployee().getReportsTo().getId() != 0) {
                Link link = linkService.BuildLinkRelated("/employee/" + dto.getSalesRepEmployee().getReportsTo().getId(), MediaType.APPLICATION_JSON);
                dto.getSalesRepEmployee().getReportsTo().setLink(link);
            }
            if (dto.getSalesRepEmployee() != null && dto.getSalesRepEmployee().getOffice() != null && dto.getSalesRepEmployee().getOffice().getId() != null) {
                Link link = linkService.BuildLinkRelated("/office/" + dto.getSalesRepEmployee().getOffice().getId(), MediaType.APPLICATION_JSON);
                dto.getSalesRepEmployee().getOffice().setLink(link);
            }
        }
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
    @Path("/{id}/details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Customer with sales rep name and payment list")
    public Response readDetails(@PathParam("id") Integer id) {
        Response response;
        try {
            Customer entity = service.getEntityById(id);
            CustomerDetailDto dto = detailMapper.toResource(entity);
            // fetch payments
            List<PaymentDto> payments = paymentMapper.toResourceList(
                    paymentService.getAllByCustomer(Collections.singletonList(id)));
            dto.setPayments(payments);
            // fetch orders
            List<OrderDto> orders = orderMapper.toResourceList(
                    orderService.getAllByCustomer(Collections.singletonList(id)));
            dto.setOrders(orders);
            // link dto
            linkDetailDTO(dto);
            response = Response.ok(dto).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
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
