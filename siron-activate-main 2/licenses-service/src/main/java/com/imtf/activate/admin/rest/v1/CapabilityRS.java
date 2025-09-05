package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.mappers.CapabilityMappers;
import com.imtf.activate.admin.rest.v1.resources.CapabilityResource;
import com.imtf.activate.admin.service.CapabilityService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;

@Path("licensing/api/v1/capabilities")
public final class CapabilityRS {

    private static final Logger LOGGER = Logger.getLogger(CapabilityRS.class);

    private final CapabilityMappers mappers = new CapabilityMappers();

    private final CapabilityService capabilityService;

    @Inject
    public CapabilityRS(CapabilityService capabilityService) {
        this.capabilityService = capabilityService;
    }


    @GET
    @RolesAllowed({"user", "license-issuer", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<CapabilityResource> getAll() {
        LOGGER.debug("Get all capabilities");
        return capabilityService.getAll()
                .stream()
                .map(mappers::toResource)
                .toList();
    }

    @POST
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CapabilityResource create(CapabilityResource resource) {
        LOGGER.debug("Create capability " + resource);
        Capability capability = capabilityService.create(mappers.toCapability(resource));
        return mappers.toResource(capability);
    }

    @PUT
    @RolesAllowed("admin")
    @Path("{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CapabilityResource update(@PathParam("oid") String oid, CapabilityResource resource) {
        LOGGER.debug("Update capability " + oid + " with " + resource);
        Capability capability = capabilityService.update(oid, mappers.toCapability(resource));
        return mappers.toResource(capability);
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("{oid}")
    public void delete(@PathParam("oid") String oid) {
        LOGGER.debug("Delete capability " + oid);
        capabilityService.delete(oid);
    }
}
