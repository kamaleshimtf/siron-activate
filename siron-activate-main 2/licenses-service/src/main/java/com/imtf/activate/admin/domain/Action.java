package com.imtf.activate.admin.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Action(UUID oid, ActionScope scope, Effect effect, String principal, LocalDateTime timestamp,
                     Object payload) {

    public Action(ActionScope scope, Effect effect, Object payload) {
        this(UUID.randomUUID(), scope, effect, "", LocalDateTime.now(), payload);
    }

    public Action(ActionScope scope, Effect effect) {
        this(scope, effect, null);
    }
}
