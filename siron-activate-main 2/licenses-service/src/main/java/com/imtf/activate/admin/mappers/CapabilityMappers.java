package com.imtf.activate.admin.mappers;

import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.domain.CapabilityFeature;
import com.imtf.activate.admin.persistence.internal.CapabilityDocument;
import com.imtf.activate.admin.persistence.internal.FeatureDocument;
import com.imtf.activate.admin.rest.v1.resources.CapabilityFeatureResource;
import com.imtf.activate.admin.rest.v1.resources.CapabilityResource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class CapabilityMappers {

    public CapabilityResource toResource(Capability capability) {
        Stream<CapabilityFeature> featureStream =
                capability.features() == null
                        ? Stream.empty()
                        : capability.features().stream();
        List<CapabilityFeatureResource> list = featureStream.map(this::toFeatureResource).toList();
        return new CapabilityResource(capability.oid().toString(), capability.name(), list);
    }

    public CapabilityFeatureResource toFeatureResource(CapabilityFeature feature) {
        return new CapabilityFeatureResource(feature.name());
    }

    public Capability toCapability(CapabilityDocument document) {
        Stream<FeatureDocument> featureDocumentStream =
                document.features() == null
                        ? Stream.empty()
                        : document.features().stream();

        List<CapabilityFeature> features = featureDocumentStream.map(this::toCapabilityFeature).toList();
        return new Capability(UUID.fromString(document.oid()), document.name(), features);
    }

    public CapabilityFeature toCapabilityFeature(FeatureDocument document) {
        return new CapabilityFeature(document.name());
    }

    public Capability toCapability(CapabilityResource resource) {
        Stream<CapabilityFeatureResource> featureResourceStream =
                resource.features() == null
                        ? Stream.empty()
                        : resource.features().stream();

        List<CapabilityFeature> features = featureResourceStream.map(this::toCapabilityFeature).toList();
        return new Capability(
                resource.oid() == null
                        ? UUID.randomUUID()
                        : UUID.fromString(resource.oid()),
                resource.name(),
                features);
    }

    public CapabilityFeature toCapabilityFeature(CapabilityFeatureResource resource) {
        return new CapabilityFeature(resource.name());
    }

    public CapabilityDocument toDocument(Capability capability) {
        Stream<CapabilityFeature> capabilitiyFeatureStream = capability.features() == null
                ? Stream.empty()
                : capability.features().stream();

        List<FeatureDocument> features = capabilitiyFeatureStream.map(this::toDocument).toList();
        return new CapabilityDocument(capability.oid().toString(), capability.name(), features);
    }

    public FeatureDocument toDocument(CapabilityFeature feature) {
        return new FeatureDocument(feature.name());
    }
}
