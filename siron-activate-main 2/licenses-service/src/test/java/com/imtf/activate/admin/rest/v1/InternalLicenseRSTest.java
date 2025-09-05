package com.imtf.activate.admin.rest.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imtf.activate.admin.mappers.InternalLicenseMappers;
import com.imtf.activate.admin.rest.v1.resources.InternalLicenseResource;
import com.imtf.activate.lib.SignedSironLicense;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(InternalLicenseRS.class)
@TestSecurity(authorizationEnabled = false)
class InternalLicenseRSTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final InternalLicenseResource createResource = new InternalLicenseResource(
            List.of("acm"),
            "ceb",
            "ceb",
            "test@imtf.com");

    @ConfigProperty(name = "siron.activate.internal.license.duration.days")
    int days;

    private InternalLicenseRSTest() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateLicenseEndpoint_200statusCode_responseIsCorrect() throws JsonProcessingException {
        ResponseEnvelop response = given()
                .header("Content-Type", "application/json")
                .body(createResource)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(ResponseEnvelop.class);

        String decodedValue = new String(Base64.getDecoder().decode(response.value()));
        SignedSironLicense license = objectMapper.readValue(decodedValue, SignedSironLicense.class);

        assertEquals(createResource.capabilities(), license.metadata().getCapabilities());
        assertEquals(createResource.customer(), license.metadata().getCustomerId());
        assertEquals(createResource.licenseInformation(), license.metadata().getCustomerName());
        assertEquals(LocalDate.now(), license.metadata().getCreationDate());
        assertEquals(LocalDate.now().plusDays(days), license.metadata().getExpirationDate());
    }

    record ResponseEnvelop(String value) {
    }
}