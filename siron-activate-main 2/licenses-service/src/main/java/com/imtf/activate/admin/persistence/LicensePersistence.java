package com.imtf.activate.admin.persistence;

import com.imtf.activate.admin.domain.License;

import java.util.List;
import java.util.Optional;

public interface LicensePersistence {

    Optional<License> get(String oid) ;

    List<License> getAll();

    void create(License license);

    License update(String oid, License license);

    void delete(String oid);
}
