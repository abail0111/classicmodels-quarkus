package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.dto.CustomerDetailDto;
import de.bail.classicmodels.model.dto.CustomerDto;
import de.bail.classicmodels.model.dto.OrderDto;
import de.bail.classicmodels.model.dto.PaymentDto;
import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.mapper.CustomerDetailMapper;
import de.bail.classicmodels.model.mapper.CustomerMapper;
import de.bail.classicmodels.model.mapper.OrderMapperNoCustomer;
import de.bail.classicmodels.model.mapper.PaymentMapperNoCustomer;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.OrderService;
import de.bail.classicmodels.service.PaymentService;
import de.bail.classicmodels.util.VCard;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/customer")
public class CustomerResource extends CrudResource<Customer, CustomerDto, Integer, CustomerService, CustomerMapper> {

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
    public void linkDTO(CustomerDto dto) {
        if (dto != null && dto.getSalesRepEmployee() != null && dto.getSalesRepEmployee().getId() != 0) {
            Link link = getLinkService().BuildLinkRelated("/employee/" + dto.getSalesRepEmployee().getId(), MediaType.APPLICATION_JSON);
            dto.getSalesRepEmployee().setLink(link);
        }
    }

    public void linkDetailDTO(CustomerDetailDto dto) {
        if (dto != null) {
            if (dto.getSalesRepEmployee() != null && dto.getSalesRepEmployee().getReportsTo() != null && dto.getSalesRepEmployee().getReportsTo().getId() != 0) {
                Link link = getLinkService().BuildLinkRelated("/employee/" + dto.getSalesRepEmployee().getReportsTo().getId(), MediaType.APPLICATION_JSON);
                dto.getSalesRepEmployee().getReportsTo().setLink(link);
            }
            if (dto.getSalesRepEmployee() != null && dto.getSalesRepEmployee().getOffice() != null && dto.getSalesRepEmployee().getOffice().getId() != null) {
                Link link = getLinkService().BuildLinkRelated("/office/" + dto.getSalesRepEmployee().getOffice().getId(), MediaType.APPLICATION_JSON);
                dto.getSalesRepEmployee().getOffice().setLink(link);
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Customer")
    @Override
    public Response create(CustomerDto entity) {
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
        Customer entity = getService().getEntityById(id);
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
        return Response.ok(dto).build();
    }

    @GET
    @Path("/{id}")
    @Consumes("text/x-vcard")
    @Produces("text/x-vcard; profile=\"vcard\"; charset=iso-8859-1")
    public Response vcard(@PathParam("id") Integer id) {
        Customer customer = getService().getEntityById(id);
        return Response.ok(VCard.createFromCustomer(customer)).build();
    }

//    @GET
//    @Path("/search")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Operation(summary = "search for customer by term")
//    public Response search(@QueryParam("term") String term) {
//        List<Customer> customers = service.search(term);
//        List<CustomerDto> dtos = mapper.toResourceList(customers);
//        // link dto
//        dtos.forEach(this::linkDTO);
//        return Response.ok(dtos).build();
//    }

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
    public Response update(@PathParam("id") Integer id, CustomerDto entity) {
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
