package com.imtf.activate.admin.rest.v1.exception;

import com.imtf.activate.admin.service.exceptions.DuplicateCustomerException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class DuplicateCustomerExceptionMapper implements ExceptionMapper<DuplicateCustomerException> {

    @Override
    public Response toResponse(DuplicateCustomerException exception) {
        return Response.status(Response.Status.CONFLICT.getStatusCode()).entity(exception.getMessage()).build();
    }
}
