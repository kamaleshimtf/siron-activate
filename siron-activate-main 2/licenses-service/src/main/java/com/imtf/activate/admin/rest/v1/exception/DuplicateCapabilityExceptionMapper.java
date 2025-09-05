package com.imtf.activate.admin.rest.v1.exception;

import com.imtf.activate.admin.service.exceptions.DuplicateCapabilityException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class DuplicateCapabilityExceptionMapper implements ExceptionMapper<DuplicateCapabilityException> {

    @Override
    public Response toResponse(DuplicateCapabilityException exception) {
        return Response.status(Response.Status.CONFLICT.getStatusCode()).entity(exception.getMessage()).build();
    }
}
