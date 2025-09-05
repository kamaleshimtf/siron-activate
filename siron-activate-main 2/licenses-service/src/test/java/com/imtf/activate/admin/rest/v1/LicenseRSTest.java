package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.rest.v1.resources.LicenseResource;
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

@QuarkusTest
@TestHTTPEndpoint(LicenseRS.class)
@TestSecurity(authorizationEnabled = false)
class LicenseRSTest {

    @Inject
    LicenseService service;

    @BeforeEach
    void cleanup() {
        service.getAll().forEach(l -> service.delete(l.oid().toString()));
    }

    final LicenseResource createResource = new LicenseResource(
            null,
            List.of("acm"),
            "acm",
            "ceb",
            "Credit Europe Bank",
            null,
            LocalDate.now(),
            null,
            null,
            "test@imtf.com");

    @Test
    void testGetLicensesEndpoint_endpointExists() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    void testCreateLicenseEndpoint_200statusCode_responseIsCorrect() {
        LicenseResource response = given()
                .header("Content-Type", "application/json")
                .body(createResource)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(LicenseResource.class);

        assertEquals(createResource.capabilities(), response.capabilities());
        assertEquals(createResource.productName(), response.productName());
        assertEquals(createResource.customerId(), response.customerId());
        assertEquals(createResource.customerName(), response.customerName());
        assertEquals(createResource.expirationDate(), response.expirationDate());
    }

    @Test
    void getLicenseByOid() {
        LicenseResource createdLicense = given()
                .header("Content-Type", "application/json")
                .body(createResource)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(LicenseResource.class);

        String licenseOid = createdLicense.oid();
        LicenseResource gettedLicense = given()
                .header("Content-Type", "application/json")
                .body(createResource)
                .when()
                .get(licenseOid)
                .then()
                .statusCode(200)
                .extract()
                .as(LicenseResource.class);

        assertEquals(createdLicense, gettedLicense);

    }
}