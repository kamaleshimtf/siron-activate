package com.imtf.activate.admin.mappers;

import com.imtf.activate.admin.domain.InternalLicense;
import com.imtf.activate.admin.rest.v1.resources.InternalLicenseResource;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDate;
import java.util.UUID;

@Singleton
public final class InternalLicenseMappers {

    @ConfigProperty(name = "siron.activate.internal.license.duration.days", defaultValue = "7")
    int days;

    public InternalLicense fromResource(InternalLicenseResource resource) {
        return new InternalLicense(
                UUID.randomUUID(),
                resource.capabilities(),
                resource.customer(),
                resource.licenseInformation(),
                LocalDate.now(),
                LocalDate.now().plusDays(days),
                resource.contactEmail()
        );
    }
}
