package com.imtf.activate.admin.rest.v1.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record InternalLicenseResource(
        List<String> capabilities,
        String customer,
        String licenseInformation,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String contactEmail) {
}
