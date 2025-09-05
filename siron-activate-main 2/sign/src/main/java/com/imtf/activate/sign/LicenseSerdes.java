package com.imtf.activate.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;

public final class LicenseSerdes {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LicenseSerdes() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String serialize(SironLicense license) {
        try {
            return objectMapper.writeValueAsString(license);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public SironLicense deserializeLicense(String license) {
        try {
            return objectMapper.readValue(license, SironLicense.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode asJsonNode(SironLicense license) {
        return objectMapper.valueToTree(license);
    }

    public String serializeSignedLicense(SignedSironLicense signedSironLicense) {
        try {
            return objectMapper.writeValueAsString(signedSironLicense);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public SignedSironLicense deserializeSignedLicense(String signedLicense) {
        try {
            return objectMapper.readValue(signedLicense, SignedSironLicense.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
