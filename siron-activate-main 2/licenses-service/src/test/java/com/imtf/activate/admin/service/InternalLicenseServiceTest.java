package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.InternalLicense;
import com.imtf.activate.admin.domain.License;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class InternalLicenseServiceTest {

    @Inject
    InternalLicenseService service;

    @Test
    void sign_signeLicense_returnBase64() {
        InternalLicense license = new InternalLicense(UUID.randomUUID(), List.of("acm"), "ceb", "credit europe", LocalDate.now(), LocalDate.now(), "t@t.com");
        String sign = service.sign(license);
        assertFalse(sign.isEmpty());
    }
}