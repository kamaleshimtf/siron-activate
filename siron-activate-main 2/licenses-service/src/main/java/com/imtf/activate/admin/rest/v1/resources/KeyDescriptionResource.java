package com.imtf.activate.admin.rest.v1.resources;

import java.time.Instant;

public final class KeyDescriptionResource {

    private String oid;

    private String name;

    private Instant creationDate;

    private int version;

    public KeyDescriptionResource() {
    }

    public KeyDescriptionResource(String oid, String name, Instant creationDate, int version) {
        this.oid = oid;
        this.name = name;
        this.creationDate = creationDate;
        this.version = version;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
