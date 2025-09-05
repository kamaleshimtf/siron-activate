package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.mappers.InternalLicenseMappers;
import com.imtf.activate.admin.rest.v1.resources.InternalLicenseResource;
import com.imtf.activate.admin.rest.v1.resources.SignedLicenseString;
import com.imtf.activate.admin.service.InternalLicenseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@ApplicationScoped
@Path("/licensing/api/v1/internal-licenses/")
public class InternalLicenseRS {

    private static final Logger LOGGER = Logger.getLogger(InternalLicenseRS.class);

    private final InternalLicenseService licenseService;

    private final InternalLicenseMappers mappers;

    @Inject
    public InternalLicenseRS(final InternalLicenseService licenseService, final InternalLicenseMappers mappers) {
        this.licenseService = licenseService;
        this.mappers = mappers;
    }

    @POST
    @RolesAllowed({"user", "license-issuer", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
    public SignedLicenseString create(InternalLicenseResource resource) {
        LOGGER.debug("Signing internal license " + resource);
        return new SignedLicenseString(licenseService.sign(mappers.fromResource(resource)));
    }
}
