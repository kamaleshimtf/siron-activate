package com.imtf.activate.admin.persistence.internal;

import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.mappers.CapabilityMappers;
import com.imtf.activate.admin.persistence.CapabilityPersistence;
import com.imtf.activate.admin.service.exceptions.DuplicateCapabilityException;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReturnDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class CapabilityPersistenceImpl implements CapabilityPersistence {

    private static final String SIRON_ACTIVATE = "siron-activate";

    private static final String CAPABILITY = "capabilities";

    private final CapabilityMappers mappers;

    private final MongoClient mongoClient;

    @Inject
    public CapabilityPersistenceImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mappers = new CapabilityMappers();
    }

    @Override
    public List<Capability> getAll() {
        List<Capability> list = new ArrayList<>();

        try (MongoCursor<CapabilityDocument> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                CapabilityDocument document = cursor.next();
                list.add(mappers.toCapability(document));
            }
        }
        return list;
    }

    @Override
    public void create(Capability capability) {
        CapabilityDocument document = mappers.toDocument(capability);

        try {
            getCollection().insertOne(document);
        } catch (MongoWriteException e) {
            if (ErrorCategory.DUPLICATE_KEY.equals(ErrorCategory.fromErrorCode(e.getCode()))) {
                throw new DuplicateCapabilityException(capability.name());
            } else {
                throw e;
            }
        }
    }

    @Override
    public Capability update(String oid, Capability capability) {
        Capability capabilityToUpdate = new Capability(UUID.fromString(oid), capability.name(), capability.features());
        CapabilityDocument documentToUpdate = mappers.toDocument(capabilityToUpdate);
        CapabilityDocument document = getCollection().findOneAndReplace(
                eq("_id", oid),
                documentToUpdate,
                new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
        );

        if (document == null) {
            throw new IllegalArgumentException("Cannot update capability: capability document with oid " + oid + " not found");
        } else {
            return mappers.toCapability(document);
        }
    }

    @Override
    public void delete(String oid) {
        getCollection().deleteOne(eq("_id", oid));
    }

    private MongoCollection<CapabilityDocument> getCollection() {
        MongoDatabase database = mongoClient.getDatabase(SIRON_ACTIVATE);
        MongoCollection<CapabilityDocument> collection = database.getCollection(CAPABILITY, CapabilityDocument.class);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(Indexes.descending("name"), indexOptions);
        return collection;
    }
}
