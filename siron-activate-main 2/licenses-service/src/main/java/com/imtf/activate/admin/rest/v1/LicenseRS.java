package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.mappers.LicenseMappers;
import com.imtf.activate.admin.rest.v1.resources.LicenseResource;
import com.imtf.activate.admin.rest.v1.resources.SignedLicenseString;
import com.imtf.activate.admin.service.LicenseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@Path("licensing/api/v1/licenses")
public final class LicenseRS {

    private static final Logger LOGGER = Logger.getLogger(LicenseRS.class);

    private final LicenseMappers mappers;

    private final LicenseService licenseService;

    @Inject
    public LicenseRS(LicenseService licenseService) {
        this.licenseService = licenseService;
        this.mappers = new LicenseMappers();
    }

    @GET
    @RolesAllowed({"user", "license-issuer", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<LicenseResource> getAll() {
        LOGGER.debug("Get all licenses");
        return licenseService.getAll()
                .stream()
                .map(mappers::toResource)
                .toList();
    }

    @GET
    @RolesAllowed({"user", "license-issuer", "admin"})
    @Path("{oid}")
    @Produces(MediaType.APPLICATION_JSON)
    public LicenseResource get(String oid) {
        LOGGER.debug("Get license with oid " + oid);
        Optional<License> optionalLicense = licenseService.get(oid);
        if (optionalLicense.isPresent()) {
            return mappers.toResource(optionalLicense.get());
        } else {
            throw new RuntimeException("license with oid " + oid + " not found");
        }
    }

    @POST
    @RolesAllowed({"license-issuer", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LicenseResource create(LicenseResource resource) {
        LOGGER.debug("Create license " + resource);
        License license = mappers.fromResource(resource);
        License signedSironLicense = licenseService.create(license);
        return mappers.toResource(signedSironLicense);
    }

    @DELETE
    @RolesAllowed({"license-issuer", "admin"})
    @Path("{oid}")
    public void delete(String oid) {
        LOGGER.debug("Delete license with oid " + oid);
        licenseService.delete(oid);
    }

    @POST
    @RolesAllowed({"license-issuer", "license-user", "admin"})
    @Path("{oid}/sign")
    @Produces(MediaType.APPLICATION_JSON)
    public SignedLicenseString sign(String oid) {
        LOGGER.debug("Sign license " + oid);
        return new SignedLicenseString(licenseService.sign(oid));
    }
}
