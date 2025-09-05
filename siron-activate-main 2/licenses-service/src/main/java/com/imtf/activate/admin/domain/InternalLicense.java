package com.imtf.activate.admin.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InternalLicense(
        UUID oid,
        List<String> capabilities,
        String customerId,
        String customerName,
        LocalDate creationDate,
        LocalDate expirationDate,
        String contactEmail) {
}
