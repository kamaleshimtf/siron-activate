package com.imtf.activate.admin.rest.v1;

import com.imtf.activate.admin.rest.v1.resources.CapabilityFeatureResource;
import com.imtf.activate.admin.rest.v1.resources.CapabilityResource;
import com.imtf.activate.admin.service.CapabilityService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(CapabilityRS.class)
@TestSecurity(authorizationEnabled = false)
class CapabilityRSTest {

    @Inject
    CapabilityService service;

    @BeforeEach
    void cleanup() {
        service.getAll().forEach(capability -> service.delete(capability.oid().toString()));
    }

    @Test
    void createCapability_returnCreatedCapability() {
        CapabilityResource capability = new CapabilityResource(null, "detect", null);

        CapabilityResource createdCapability = given()
                .header("Content-Type", "application/json")
                .body(capability)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(CapabilityResource.class);

        assertNotNull(createdCapability.oid());
        assertEquals("detect", createdCapability.name());
        assertNotNull(createdCapability.features());
        assertTrue(createdCapability.features().isEmpty());
    }

    @Test
    void createCapabilityWithFeatures_returnCreatedCapability() {
        CapabilityFeatureResource featureA = new CapabilityFeatureResource("featureA");
        CapabilityFeatureResource featureB = new CapabilityFeatureResource("featureB");
        CapabilityResource capability = new CapabilityResource(null, "detect", List.of(featureA, featureB));

        CapabilityResource createdCapability = given()
                .header("Content-Type", "application/json")
                .body(capability)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(CapabilityResource.class);

        assertNotNull(createdCapability.oid());
        assertEquals("detect", createdCapability.name());
        assertNotNull(createdCapability.features());
        assertFalse(createdCapability.features().isEmpty());
        assertTrue(createdCapability.features().contains(featureA));
        assertTrue(createdCapability.features().contains(featureB));
    }

    @Disabled("not yet implemented")
    @Test
    void createCapability_getCapability_capabilitiesAreEquals() {

    }

    @Disabled("not yet implemented")
    @Test
    void createCapabilityWithFeatures_getCapability_capabilitiesAreEquals() {

    }
}