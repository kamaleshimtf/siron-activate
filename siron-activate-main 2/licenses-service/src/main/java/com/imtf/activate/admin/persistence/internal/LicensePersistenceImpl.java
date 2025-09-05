package com.imtf.activate.admin.persistence.internal;

import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.mappers.LicenseMappers;
import com.imtf.activate.admin.persistence.LicensePersistence;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;


@ApplicationScoped
public class LicensePersistenceImpl implements LicensePersistence {

    private static final String SIRON_ACTIVATE = "siron-activate";

    private static final String LICENSES = "licenses";

    private final LicenseMappers mappers;

    private final MongoClient mongoClient;

    @Inject
    public LicensePersistenceImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mappers = new LicenseMappers();
    }

    @Override
    public Optional<License> get(String oid) {
        LicenseDocument document = getCollection().find(eq("_id", oid)).first();
        return document == null
                ? Optional.empty()
                : Optional.of(mappers.fromDocument(document));
    }

    public List<License> getAll() {
        List<License> list = new ArrayList<>();

        try (MongoCursor<LicenseDocument> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                LicenseDocument document = cursor.next();
                list.add(mappers.fromDocument(document));
            }
        }
        return list;
    }

    @Override
    public void create(License license) {
        LicenseDocument document = mappers.toDocument(license);
        getCollection().insertOne(document);
    }

    @Override
    public License update(String oid, License license) {
        License licenseToUpdate = new License(UUID.fromString(oid), license.productName(), license.customer(), license.information(), license.creationDate(), license.expirationDate(), license.contactEmail());
        LicenseDocument documentToUpdate = mappers.toDocument(licenseToUpdate);

        LicenseDocument document = getCollection().findOneAndReplace(
                eq("_id", oid),
                documentToUpdate,
                new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
        );

        if (document == null) {
            throw new IllegalArgumentException("Cannot update license: license document with oid " + oid + " not found");
        } else {
            return mappers.fromDocument(document);
        }

    }

    @Override
    public void delete(String oid) {
        getCollection().deleteOne(eq("_id", oid));
    }

    private MongoCollection<LicenseDocument> getCollection() {
        MongoDatabase database = mongoClient.getDatabase(SIRON_ACTIVATE);
        return database.getCollection(LICENSES, LicenseDocument.class);
    }
}

