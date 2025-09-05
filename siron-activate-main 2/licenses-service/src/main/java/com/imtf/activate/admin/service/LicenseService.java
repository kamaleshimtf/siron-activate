package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.domain.ActionScope;
import com.imtf.activate.admin.domain.Effect;
import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.persistence.LicensePersistence;
import com.imtf.activate.admin.service.keys.KeyService;
import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;
import com.imtf.activate.sign.Licenses;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LicenseService {

    private final LicensePersistence persistence;

    private final KeyService keyService;

    private final AuditService auditService;

    private final SecurityIdentity securityIdentity;

    @Inject
    public LicenseService(LicensePersistence persistence, KeyService keyService, AuditService auditService, SecurityIdentity securityIdentity) {
        this.persistence = persistence;
        this.keyService = keyService;
        this.auditService = auditService;
        this.securityIdentity = securityIdentity;
    }

    public Optional<License> get(String oid) {
        return persistence.get(oid);
    }

    public List<License> getAll() {
        return persistence.getAll();
    }

    public License create(License license) {
        persistence.create(license);
        auditService.log(new Action(ActionScope.LICENSE, Effect.LicenseEffect.CREATE, license));
        return license;
    }

    public void delete(String oid) {
        persistence.delete(oid);
    }

    public String sign(String oid) {
        License license = get(oid).orElseThrow();
        SignedSironLicense signedSironLicense = keyService.sign(new SironLicense(license.capabilities(), license.customer(), license.information(), license.creationDate(), license.expirationDate(), null));
        auditService.log(new Action(ActionScope.LICENSE, Effect.LicenseEffect.SIGN, license));
        return Licenses.asBase64(signedSironLicense);
    }
}
