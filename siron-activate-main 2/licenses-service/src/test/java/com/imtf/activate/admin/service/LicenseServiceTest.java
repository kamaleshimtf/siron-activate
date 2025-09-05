package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.service.exceptions.LicenseStateException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class LicenseServiceTest {

    @Inject
    LicenseService service;

    @BeforeEach
    void cleanup() {
        service.getAll().forEach(license -> service.delete(license.oid().toString()));
    }

    @Test
    void getAll_noLicense_returnEmptyGetAll() {
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void getAll_createLicenses_returnLicenses() {
        License acm = new License(UUID.randomUUID(), "acm", "ceb", "credit europe", LocalDate.now(), LocalDate.now());
        License detect = new License(UUID.randomUUID(), "detect", "raiffeisen", "raiffeisen switzerland", LocalDate.now(), LocalDate.now());
        License fm = new License(UUID.randomUUID(), "fm", "ubp", "union bank privé", LocalDate.now(), LocalDate.now());

        service.create(acm);
        service.create(detect);
        service.create(fm);

        List<License> licenses = service.getAll();
        assertEquals(3, licenses.size());

        for (License license : licenses) {
            switch (license.productName()) {
                case "acm":
                    assertEquals(acm, license);
                    break;
                case "detect":
                    assertEquals(detect, license);
                    break;
                case "fm":
                    assertEquals(fm, license);
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    void get_createLicenses_returnLicenseById() {
        License acm = new License(UUID.randomUUID(), "acm", "ceb", "credit europe", LocalDate.now(), LocalDate.now());
        License detect = new License(UUID.randomUUID(), "detect", "raiffeisen", "raiffeisen switzerland", LocalDate.now(), LocalDate.now());
        License fm = new License(UUID.randomUUID(), "fm", "ubp", "union bank privé", LocalDate.now(), LocalDate.now());

        service.create(acm);
        service.create(detect);
        service.create(fm);

        List<License> licenses = service.getAll();
        assertEquals(3, licenses.size());

        assertEquals(acm, service.get(acm.oid().toString()).get());
        assertEquals(detect, service.get(detect.oid().toString()).get());
        assertEquals(fm, service.get(fm.oid().toString()).get());
    }

    @Test
    void create_license_returnCreated() {
        License license = new License(UUID.randomUUID(), "acm", "ceb", "credit europe", LocalDate.now(), LocalDate.now());
        License createdLicense = service.create(license);
        assertEquals(license, createdLicense);
    }

    @Test
    void delete_createAndDeleteLicense_licenseDeleted() {
        License license = new License(UUID.randomUUID(), "acm", "ceb", "credit europe", LocalDate.now(), LocalDate.now());
        service.create(license);
        assertEquals(1, service.getAll().size());
        service.delete(license.oid().toString());
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void sign_signeLicense_returnBase64() {
        License license = new License(UUID.randomUUID(), "acm", "ceb", "credit europe", LocalDate.now(), LocalDate.now());
        service.create(license);
        String sign = service.sign(license.oid().toString());
        assertFalse(sign.isEmpty());
    }

    @Test
    void delete_createLicenseAndDelete_licenseDeleted() {
        License license = new License(UUID.randomUUID(), "acm", "ceb", "credit europe", LocalDate.now(), LocalDate.now());
        service.create(license);
        service.delete(license.oid().toString());
        assertTrue(service.get(license.oid().toString()).isEmpty());
    }
}