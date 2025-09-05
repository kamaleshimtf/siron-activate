package com.imtf.activate.admin.persistence.internal;

import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;

public record ActionDocument(
        @BsonId String oid,
        String scope,
        String effect,
        String principal,
        LocalDateTime timestamp,
        String payload) {
}
