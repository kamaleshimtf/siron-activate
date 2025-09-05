package com.imtf.activate.admin.persistence.internal;

import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.mappers.CustomerMappers;
import com.imtf.activate.admin.persistence.CustomerPersistence;
import com.imtf.activate.admin.service.exceptions.DuplicateCustomerException;
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
public class CustomerPersistenceImpl implements CustomerPersistence {

    private static final String SIRON_ACTIVATE = "siron-activate";

    private static final String CUSTOMERS = "customers";

    private final CustomerMappers mappers;

    private final MongoClient mongoClient;

    @Inject
    public CustomerPersistenceImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mappers = new CustomerMappers();
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();

        try (MongoCursor<CustomerDocument> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                CustomerDocument document = cursor.next();
                list.add(mappers.fromDocument(document));
            }
        }
        return list;
    }

    @Override
    public void create(Customer customer) {
        CustomerDocument document = mappers.toDocument(customer);

        try {
            getCollection().insertOne(document);
        } catch (MongoWriteException e) {
            if (ErrorCategory.DUPLICATE_KEY.equals(ErrorCategory.fromErrorCode(e.getCode()))) {
                throw new DuplicateCustomerException(customer.name());
            } else {
                throw e;
            }
        }
    }

    @Override
    public Customer update(String oid, Customer customer) {
        Customer customerToUpdate = new Customer(UUID.fromString(oid), customer.name());
        CustomerDocument documentToUpdate = mappers.toDocument(customerToUpdate);
        CustomerDocument document = getCollection().findOneAndReplace(
                eq("_id", oid),
                documentToUpdate,
                new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
        );

        if (document == null) {
            throw new IllegalArgumentException("Cannot update customer: customer document with oid " + customer.oid() + " not found");
        } else {
            return mappers.fromDocument(document);
        }
    }

    @Override
    public void delete(String oid) {
        getCollection().deleteOne(eq("_id", oid));
    }

    private MongoCollection<CustomerDocument> getCollection() {
        MongoDatabase database = mongoClient.getDatabase(SIRON_ACTIVATE);
        MongoCollection<CustomerDocument> collection = database.getCollection(CUSTOMERS, CustomerDocument.class);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(Indexes.descending("name"), indexOptions);
        return collection;
    }
}
