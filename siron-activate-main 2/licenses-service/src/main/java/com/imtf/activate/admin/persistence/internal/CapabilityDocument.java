package com.imtf.activate.admin.persistence.internal;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.List;

public record CapabilityDocument(@BsonId String oid, String name, List<FeatureDocument> features) {
}
