package com.imtf.activate.admin.mappers;

import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.persistence.internal.LicenseDocument;
import com.imtf.activate.admin.rest.v1.resources.LicenseResource;
import com.imtf.activate.admin.rest.v1.resources.SignedLicenseResource;
import com.imtf.activate.lib.SignedSironLicense;

import java.time.LocalDate;
import java.util.UUID;

public final class LicenseMappers {

    public LicenseDocument toDocument(License license) {
        return new LicenseDocument(
                license.oid().toString(),
                license.capabilities(),
                license.productName(),
                license.customer(),
                license.information(),
                license.creationDate(),
                license.expirationDate(),
                license.contactEmail());
    }

    public License fromDocument(LicenseDocument document) {
        return new License(
                UUID.fromString(document.oid()),
                document.capabilities(),
                document.productName(),
                document.customerId(),
                document.customerName(),
                document.creationDate(),
                document.expirationDate(),
                document.contactEmail());
    }

    public LicenseResource toResource(License license) {
        return new LicenseResource(
                license.oid().toString(),
                license.capabilities(),
                license.productName(),
                license.customer(),
                license.information(),
                license.creationDate(),
                license.expirationDate(),
                null,
                null,
                license.contactEmail());
    }

    public SignedLicenseResource toSignedResource(SignedSironLicense license) {
        throw new UnsupportedOperationException("not implemented");
    }

    public License fromResource(LicenseResource resource) {
        return new License(
                resource.oid() == null ? UUID.randomUUID() : UUID.fromString(resource.oid()),
                resource.capabilities(),
                resource.productName(),
                resource.customerId(),
                resource.customerName(),
                resource.creationDate() == null ? LocalDate.now() : resource.creationDate(),
                resource.expirationDate(),
                resource.contactEmail());
    }
}
