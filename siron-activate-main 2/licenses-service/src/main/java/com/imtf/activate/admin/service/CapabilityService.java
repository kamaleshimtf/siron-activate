package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.domain.ActionScope;
import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.domain.Effect;
import com.imtf.activate.admin.persistence.CapabilityPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CapabilityService {

    private final CapabilityPersistence persistence;

    private final AuditService auditService;

    @Inject
    public CapabilityService(CapabilityPersistence persistence, AuditService auditService) {
        this.persistence = persistence;
        this.auditService = auditService;
    }

    public List<Capability> getAll() {
        return persistence.getAll();
    }

    public Capability create(Capability capability) {
        persistence.create(capability);
        auditService.log(new Action(ActionScope.CAPABILITY, Effect.CapabilityEffect.CREATE, capability));
        return capability;
    }

    public Capability update(String oid, Capability capability) {
        return persistence.update(oid, capability);
    }

    public void delete(String oid) {
        persistence.delete(oid);
    }
}
