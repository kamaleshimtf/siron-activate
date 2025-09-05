package com.imtf.activate.admin.rest.v1.exception;

import com.imtf.activate.admin.service.exceptions.LicenseStateException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class LicenseStateExceptionMapper implements ExceptionMapper<LicenseStateException> {

    @Override
    public Response toResponse(LicenseStateException exception) {
        return Response.status(Response.Status.FORBIDDEN.getStatusCode()).entity(exception.getMessage()).build();
    }
}
