package com.imtf.activate.admin.rest.v1.resources;

import java.util.List;

public record CapabilityResource(String oid, String name, List<CapabilityFeatureResource> features) {
}
