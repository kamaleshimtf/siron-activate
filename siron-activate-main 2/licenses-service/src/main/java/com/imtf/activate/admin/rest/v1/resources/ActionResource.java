package com.imtf.activate.admin.rest.v1.resources;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record ActionResource(
        String oid,
        String scope,
        String effect,
        String principal,
        LocalDateTime timestamp,
        Object payload
) {
}
