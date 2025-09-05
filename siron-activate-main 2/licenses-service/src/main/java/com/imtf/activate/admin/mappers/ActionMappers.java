package com.imtf.activate.admin.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.domain.ActionScope;
import com.imtf.activate.admin.domain.Effect;
import com.imtf.activate.admin.persistence.internal.ActionDocument;
import com.imtf.activate.admin.rest.v1.resources.ActionResource;

import java.util.UUID;

public final class ActionMappers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ActionMappers() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public ActionResource toResource(Action action) {
        String effect = toString(action.scope(), action.effect());

        return new ActionResource(
                action.oid().toString(),
                action.scope().name(),
                effect,
                action.principal(),
                action.timestamp(),
                action.payload()
        );
    }

    public ActionDocument toString(Action action) {
        String effect = toString(action.scope(), action.effect());
        String stringPayload = toStringPayload(action.payload());

        return new ActionDocument(
                action.oid().toString(),
                action.scope().name(),
                effect,
                action.principal(),
                action.timestamp(),
                stringPayload
        );
    }

    public Action fromString(ActionDocument document) {
        ActionScope actionScope = ActionScope.valueOf(document.scope());
        Effect effect = fromString(actionScope, document.effect());
        JsonNode payload = toJsonNode(document.payload());

        return new Action(
                UUID.fromString(document.oid()),
                actionScope,
                effect,
                document.principal(),
                document.timestamp(),
                payload
        );
    }

    private Effect fromString(ActionScope scope, String effect) {
        if (ActionScope.CAPABILITY.equals(scope)) {
            return Effect.CapabilityEffect.valueOf(effect);
        } else if (ActionScope.CUSTOMER.equals(scope)) {
            return Effect.CustomerEffect.valueOf(effect);
        } else if (ActionScope.LICENSE.equals(scope)) {
            return Effect.LicenseEffect.valueOf(effect);
        } else if (ActionScope.INTERNAL_LICENSE.equals(scope)) {
            return Effect.InternalLicenseEffect.valueOf(effect);
        } else {
            throw new IllegalArgumentException("Effect " + effect + " for scope " + scope + " not found");
        }
    }

    private String toString(ActionScope scope, Effect effect) {
        if (ActionScope.CAPABILITY.equals(scope)) {
            return ((Effect.CapabilityEffect) effect).name();
        } else if (ActionScope.CUSTOMER.equals(scope)) {
            return ((Effect.CustomerEffect) effect).name();
        } else if (ActionScope.LICENSE.equals(scope)) {
            return ((Effect.LicenseEffect) effect).name();
        } else if (ActionScope.INTERNAL_LICENSE.equals(scope)) {
            return ((Effect.InternalLicenseEffect) effect).name();
        } else {
            throw new IllegalArgumentException("Effect " + effect + " for scope " + scope + " not found");
        }
    }

    private String toStringPayload(Object payload) {
        if (payload == null) {
            return "";
        } else {
            try {
                return objectMapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JsonNode toJsonNode(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        } else {
            try {
                return objectMapper.readValue(payload, JsonNode.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
