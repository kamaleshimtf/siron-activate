package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.rest.v1.resources.LicenseResource;
import com.imtf.activate.admin.service.CapabilityService;
import com.imtf.activate.admin.service.CustomerService;
import com.imtf.activate.admin.service.LicenseService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(JsonLoaderRS.class)
@TestSecurity(authorizationEnabled = false)
class JsonLoaderRSTest {

    final LicenseResource licenseOne = new LicenseResource(
            null,
            List.of("acm", "aml"),
            "acm",
            "imtf-softops",
            "Software Operations",
            null,
            LocalDate.now(),
            null,
            null,
            "test@imtf.com");

    final LicenseResource licenseTwo = new LicenseResource(
            null,
            List.of("ccm", "namescreening"),
            "",
            "imtf-dev",
            "Development internal",
            null,
            LocalDate.now(),
            null,
            null,
            "test@imtf.com");

    @Inject
    JsonLoaderRS service;

    @Inject
    LicenseService licenseService;

    @Inject
    CapabilityService capabilityService;

    @Inject
    CustomerService customerService;

    @BeforeEach
    void cleanup() {
        capabilityService.getAll().forEach(capability -> capabilityService.delete(capability.oid().toString()));
        customerService.getAll().forEach(customer -> customerService.delete(customer.oid().toString()));
        licenseService.getAll().forEach(license -> licenseService.delete(license.oid().toString()));
    }

    @Test
    void load_2licenses_2LicensesCreated() {
        given()
                .header("Content-Type", "application/json")
                .body(List.of(licenseOne, licenseTwo))
                .when()
                .post("load")
                .then()
                .statusCode(204);

        assertEquals(2, licenseService.getAll().size());
    }

    @Test
    void load_2licenses_return204() {
        given()
                .header("Content-Type", "application/json")
                .body(List.of(licenseOne, licenseTwo))
                .when()
                .post("load")
                .then()
                .statusCode(204);
    }

    @Test
    void load_noLicenses_return204() {
        given()
                .header("Content-Type", "application/json")
                .body(List.of())
                .when()
                .post("load")
                .then()
                .statusCode(204);
    }

    @Test
    void load_2licenses_downloadReturn2Licenses() {
        given()
                .header("Content-Type", "application/json")
                .body(List.of(licenseOne, licenseTwo))
                .when()
                .post("load")
                .then()
                .statusCode(204);

        List<LicenseResource> licenses = given()
                .when()
                .get("download")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getList("", LicenseResource.class);

        assertEquals(2, licenses.size());
    }

    @Test
    void load_noLicenses_downloadReturnNoLicenses() {
        given()
                .header("Content-Type", "application/json")
                .body(List.of())
                .when()
                .post("load")
                .then()
                .statusCode(204);

        List<LicenseResource> licenses = given()
                .when()
                .get("download")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getList("", LicenseResource.class);

        assertTrue(licenses.isEmpty());
    }
}