package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.mappers.ActionMappers;
import com.imtf.activate.admin.rest.v1.resources.ActionResource;
import com.imtf.activate.admin.service.AuditService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;

@Path("licensing/api/v1/audit")
public final class AuditRS {

    private static final Logger LOGGER = Logger.getLogger(AuditRS.class);

    private final AuditService auditService;

    private final ActionMappers mappers;

    @Inject
    public AuditRS(AuditService auditService) {
        this.auditService = auditService;
        this.mappers = new ActionMappers();
    }

    @GET
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<ActionResource> getAll() {
        LOGGER.debug("Get all audit logs");

        return auditService.getAll()
                .stream()
                .map(mappers::toResource)
                .toList();
    }
}
