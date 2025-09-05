package com.imtf.activate.admin.persistence.internal;

import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDate;
import java.util.List;

public record LicenseDocument(
        @BsonId String oid,
        List<String> capabilities,
        String productName,
        String customerId,
        String customerName,
        LocalDate creationDate,
        LocalDate expirationDate,
        String contactEmail) {
}
