package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.domain.CapabilityFeature;
import com.imtf.activate.admin.service.exceptions.DuplicateCapabilityException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CapabilityServiceTest {

    @Inject
    CapabilityService service;

    @BeforeEach
    void cleanup() {
        service.getAll().forEach(capability -> service.delete(capability.oid().toString()));
    }

    @Test
    void list_emptyCapabilities_returnEmptyGetAll() {
        List<Capability> capabilities = service.getAll();
        assertTrue(capabilities.isEmpty());
    }

    @Test
    void getAll_addCapabilities_returnCapabilities() {
        service.create(new Capability("acm", List.of(new CapabilityFeature("featureA"))));
        service.create(new Capability("detect", List.of(new CapabilityFeature("featureB"))));
        service.create(new Capability("ccm", Collections.emptyList()));

        List<Capability> capabilities = service.getAll();
        assertEquals(3, capabilities.size());

        for (Capability capability : capabilities) {
            if ("acm".equals(capability.name())) {
                assertEquals(1, capability.features().size());
                assertEquals("featureA", capability.features().get(0).name());
            } else if ("detect".equals(capability.name())) {
                assertEquals(1, capability.features().size());
                assertEquals("featureB", capability.features().get(0).name());
            } else if ("ccm".equals(capability.name())) {
                assertTrue(capability.features().isEmpty());
            }
        }
    }

    @Test
    void create_addCapabilitiy_capabilitiyReturned() {
        Capability capability = new Capability("acm", List.of(new CapabilityFeature("featureA")));
        Capability createdCapability = service.create(capability);
        assertEquals(capability, createdCapability);
    }

    @Test
    void create_withOid_returnedWithOid() {
        Capability capability = new Capability(UUID.randomUUID(), "acm", List.of(new CapabilityFeature("featureA")));
        Capability createdCapability = service.create(capability);
        assertEquals(capability, createdCapability);
    }

    @Test
    void update_updateName_nameUpdated() {
        Capability capability = new Capability("acm", List.of(new CapabilityFeature("featureA")));
        Capability createdCapability = service.create(capability);
        Capability updatedCapability = service.update(createdCapability.oid().toString(), new Capability("detect", createdCapability.features()));
        assertEquals(createdCapability.oid(), updatedCapability.oid());
        assertEquals("detect", updatedCapability.name());
        assertEquals(createdCapability.features(), updatedCapability.features());

    }

    @Test
    void create_twice_errorReturned() {
        Capability capability = new Capability("acm", List.of(new CapabilityFeature("featureA")));
        service.create(capability);
        assertThrows(DuplicateCapabilityException.class, () -> service.create(capability));
    }
}