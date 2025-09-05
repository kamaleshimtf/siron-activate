package com.imtf.activate.admin.service.keys;

import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;
import com.imtf.activate.sign.Keys;
import com.imtf.activate.sign.LicenseSigner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class KeyService {

    private final KeyAccess awsAccess;

    private Key cachedKey;

    private KeyPair cachedKeyPair;

    @Inject
    public KeyService(KeyAccess awsAccess) {
        this.awsAccess = awsAccess;
        reloadCache();
    }

    public SignedSironLicense sign(SironLicense license) {
        if (Objects.isNull(cachedKeyPair)) {
            reloadCache();
        }

        LicenseSigner licenseSigner = new LicenseSigner(cachedKeyPair);
        return licenseSigner.sign(license);
    }

    public List<KeyDescription> getKeyDescriptions() {
        return List.of(cachedKey.description());
    }

    public void reloadCache() {
        try {
            this.cachedKey = this.awsAccess.getKeyPair();
            String secret = cachedKey.secret();
            this.cachedKeyPair = Keys.readKeyPair(secret);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
