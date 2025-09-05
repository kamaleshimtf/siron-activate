package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.mappers.LicenseMappers;
import com.imtf.activate.admin.rest.v1.resources.LicenseResource;
import com.imtf.activate.admin.service.JsonLoaderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Path("licensing/api/v1/json/")
public final class JsonLoaderRS {

    private static final Logger LOGGER = Logger.getLogger(JsonLoaderRS.class);

    private final JsonLoaderService loaderService;

    private final LicenseMappers mappers = new LicenseMappers();

    @Inject
    public JsonLoaderRS(JsonLoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @POST
    @RolesAllowed("admin")
    @Path("load")
    @Consumes(MediaType.APPLICATION_JSON)
    public void load(List<LicenseResource> resources) {
        LOGGER.info("Loading JSON licenses");
        final List<License> licenses = resources.stream().map(mappers::fromResource).collect(Collectors.toList());
        loaderService.load(licenses);
    }

    @GET
    @RolesAllowed("admin")
    @Path("download")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LicenseResource> download() {
        LOGGER.info("Downloading JSON licenses");
        List<License> licenses = loaderService.download();
        return licenses.stream().map(mappers::toResource).collect(Collectors.toList());
    }
}
