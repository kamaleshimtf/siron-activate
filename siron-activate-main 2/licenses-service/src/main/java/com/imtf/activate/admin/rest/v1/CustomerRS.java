package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.mappers.CustomerMappers;
import com.imtf.activate.admin.rest.v1.resources.CustomerResource;
import com.imtf.activate.admin.service.CustomerService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;

@Path("licensing/api/v1/customers")
public final class CustomerRS {

    private static final Logger LOGGER = Logger.getLogger(CustomerRS.class);

    private final CustomerMappers mappers = new CustomerMappers();

    private final CustomerService customerService;

    @Inject
    public CustomerRS(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GET
    @RolesAllowed({"user", "license-issuer", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerResource> getAll() {
        LOGGER.debug("Get all customers");
        return customerService.getAll().stream().map(mappers::toResource).toList();
    }

    @POST
    @RolesAllowed({"license-issuer", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerResource create(CustomerResource resource) {
        LOGGER.debug("Create customer " + resource);
        Customer customer = customerService.create(mappers.fromResource(resource));
        return mappers.toResource(customer);
    }

    @PUT
    @Path("{oid}")
    @RolesAllowed({"license-issuer", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerResource update(@PathParam("oid") String oid, CustomerResource resource) {
        LOGGER.debug("Update customer " + oid + " with " + resource);
        Customer customer = customerService.update(oid, mappers.fromResource(resource));
        return mappers.toResource(customer);
    }

    @DELETE
    @Path("{oid}")
    @RolesAllowed({"admin"})
    public void delete(@PathParam("oid") String oid) {
        LOGGER.debug("Delete customer " + oid);
        customerService.delete(oid);
    }
}
