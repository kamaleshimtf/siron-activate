package com.imtf.activate.admin.persistence.internal;

import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.mappers.ActionMappers;
import com.imtf.activate.admin.persistence.AuditPersistence;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DbAuditPersistence implements AuditPersistence {

    private static final String SIRON_ACTIVATE = "siron-activate";

    private static final String AUDIT = "audit";

    private final MongoClient mongoClient;

    private final ActionMappers mappers;

    @Inject
    public DbAuditPersistence(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mappers = new ActionMappers();
    }

    @Override
    public void log(Action action) {
        ActionDocument document = mappers.toString(action);
        getCollection().insertOne(document);
    }

    @Override
    public List<Action> getAll() {
        List<Action> list = new ArrayList<>();

        try (MongoCursor<ActionDocument> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                ActionDocument document = cursor.next();
                list.add(mappers.fromString(document));
            }
        }
        return list;
    }

    private MongoCollection<ActionDocument> getCollection() {
        MongoDatabase database = mongoClient.getDatabase(SIRON_ACTIVATE);
        return database.getCollection(AUDIT, ActionDocument.class);
    }
}
