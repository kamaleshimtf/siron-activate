package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.License;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class JsonLoaderServiceTest {

    private final List<License> licenses = List.of(
            new License(
                    UUID.randomUUID(),
                    List.of("aml", "acm"),
                    "acm",
                    "imtf-internal-demo",
                    "IMTF (internal demo)",
                    LocalDate.now(),
                    LocalDate.now(),
                    "v@t.c"
            ),
            new License(
                    UUID.randomUUID(),
                    List.of("detect", "ccm"),
                    "",
                    "imtf-internal-dev",
                    "IMTF (internal dev)",
                    LocalDate.now(),
                    LocalDate.now(),
                    "v@t.c"
            )
    );

    @Inject
    JsonLoaderService loader;

    @Inject
    LicenseService licenseService;

    @Inject
    CapabilityService capabilityService;

    @Inject
    CustomerService customerService;

    @BeforeEach
    void reset() {
        licenseService.getAll().forEach(license -> licenseService.delete(license.oid().toString()));
        capabilityService.getAll().forEach(capability -> capabilityService.delete(capability.oid().toString()));
        customerService.getAll().forEach(customer -> customerService.delete(customer.oid().toString()));
    }

    @Test
    void load_testfile_licensesAreLoaded() {
        loader.load(licenses);
        List<License> licenses = licenseService.getAll();
        assertEquals(2, licenses.size());
    }

    @Test
    void unload_testfile_exportFileCreated() {
        loader.load(licenses);
        List<License> downloadedLicenses = loader.download();
        assertEquals(licenses, downloadedLicenses);
    }
}