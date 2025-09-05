package com.imtf.activate.admin.rest.v1.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record LicenseResource(
        String oid,
        List<String> capabilities,
        @Deprecated(since = "api v1.1.0")
        String productName,
        String customerId,
        String customerName,
        LocalDate creationDate,
        LocalDate expirationDate,
        @Deprecated(since = "api v1.1.0")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime signatureDate,
        @Deprecated(since = "api v1.1.0")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String signedBy,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String contactEmail) {
}
