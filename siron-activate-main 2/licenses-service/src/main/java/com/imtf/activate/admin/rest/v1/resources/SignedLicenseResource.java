package com.imtf.activate.admin.rest.v1.resources;

public record SignedLicenseResource(LicenseResource resource, String signature) {}
