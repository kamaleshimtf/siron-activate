package com.imtf.activate.admin.persistence.internal;

import org.bson.codecs.pojo.annotations.BsonId;

public record CustomerDocument(@BsonId String oid, String name) {
}
