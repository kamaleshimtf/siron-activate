package com.imtf.activate.admin.service.keys;

import java.time.Instant;
import java.util.UUID;

public final class KeyDescription {

    private final UUID uuid;

    private final String name;

    private final Instant creationDate;

    private final int version;

    public KeyDescription(UUID uuid, String name, Instant creationDate, int version) {
        this.uuid = uuid;
        this.name = name;
        this.creationDate = creationDate;
        this.version = version;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public int getVersion() {
        return version;
    }
}
