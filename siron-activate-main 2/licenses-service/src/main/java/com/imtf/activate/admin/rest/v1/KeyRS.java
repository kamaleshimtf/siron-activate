package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.rest.v1.resources.KeyDescriptionResource;
import com.imtf.activate.admin.service.keys.KeyService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("licensing/api/v1/keys")
public final class KeyRS {

    private static final Logger LOGGER = Logger.getLogger(KeyRS.class);

    private final KeyService keyService;

    @Inject
    public KeyRS(KeyService keyService) {
        this.keyService = keyService;
    }


    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyDescriptionResource> getKeys() {
        LOGGER.debug("Loading keys description");
        return keyService.getKeyDescriptions()
                .stream()
                .map(k -> new KeyDescriptionResource(k.getUuid().toString(), k.getName(), k.getCreationDate(), k.getVersion()))
                .toList();
    }

    @POST
    @RolesAllowed("admin")
    @Path("{oid}/reload")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reload(@PathParam("oid") String oid) {
        LOGGER.debug("Reloading keys cache");
        keyService.reloadCache();
        return Response.ok().build();
    }
}
