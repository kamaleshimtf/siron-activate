package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.domain.ActionScope;
import com.imtf.activate.admin.domain.Effect;
import com.imtf.activate.admin.domain.InternalLicense;
import com.imtf.activate.admin.service.keys.KeyService;
import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;
import com.imtf.activate.sign.Licenses;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InternalLicenseService {

    private static final Logger LOGGER = Logger.getLogger(InternalLicenseService.class);

    private final KeyService keyService;

    private final AuditService auditService;

    @Inject
    public InternalLicenseService(KeyService keyService, AuditService auditService) {
        this.keyService = keyService;
        this.auditService = auditService;
    }

    public String sign(final InternalLicense license) {
        LOGGER.debug("Signing internal license " + license);
        SignedSironLicense signedSironLicense = keyService.sign(new SironLicense(license.capabilities(), license.customerId(), license.customerName(), license.creationDate(), license.expirationDate(), null));
        auditService.log(new Action(ActionScope.INTERNAL_LICENSE, Effect.InternalLicenseEffect.SIGN, license));
        return Licenses.asBase64(signedSironLicense);
    }
}
