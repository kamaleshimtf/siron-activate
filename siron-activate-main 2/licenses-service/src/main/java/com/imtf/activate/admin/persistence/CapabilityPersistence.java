package com.imtf.activate.admin.persistence;

import com.imtf.activate.admin.domain.Capability;

import java.util.List;

public interface CapabilityPersistence {

    List<Capability> getAll();

    void create(Capability capability);

    Capability update(String oid, Capability capability);

    void delete(String oid);
}
