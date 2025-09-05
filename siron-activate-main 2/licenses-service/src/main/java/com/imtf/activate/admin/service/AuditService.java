package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.persistence.AuditPersistence;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AuditService {

    private final AuditPersistence auditPersistence;

    private final SecurityIdentity securityIdentity;

    @Inject
    public AuditService(AuditPersistence auditPersistence, SecurityIdentity securityIdentity) {
        this.auditPersistence = auditPersistence;
        this.securityIdentity = securityIdentity;
    }

    public void log(Action action) {
        LocalDateTime timestamp = LocalDateTime.now();
        String principal = securityIdentity.getPrincipal().getName();
        Action enriched = new Action(action.oid(), action.scope(), action.effect(), principal, timestamp, action.payload());
        auditPersistence.log(enriched);
    }

    public List<Action> getAll() {
        return auditPersistence.getAll();
    }
}
