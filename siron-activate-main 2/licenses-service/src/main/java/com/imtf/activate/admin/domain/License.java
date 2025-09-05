package com.imtf.activate.admin.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record License(
        UUID oid,
        List<String> capabilities,
        @Deprecated(since = "api v1.1.0") String productName,
        String customer,
        String information,
        LocalDate creationDate,
        LocalDate expirationDate,
        String contactEmail) {

    public License(UUID oid, String productName, String customer, String information, LocalDate creationDate, LocalDate expirationDate) {
        this(oid, List.of(productName), productName, customer, information, creationDate, expirationDate, null);
    }

    public License(UUID oid, String productName, String customer, String information, LocalDate creationDate, LocalDate expirationDate, String contactEmail) {
        this(oid, List.of(productName), productName, customer, information, creationDate, expirationDate, contactEmail);
    }
}
