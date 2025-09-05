package com.imtf.activate.admin.rest.v1.mocks;

import com.imtf.activate.admin.service.keys.Key;
import com.imtf.activate.admin.service.keys.KeyAccess;
import com.imtf.activate.admin.service.keys.KeyDescription;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.UUID;

public class MockKeyAccess implements KeyAccess {

    @Override
    public Key getKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new Key(new KeyDescription(UUID.randomUUID(), "mock-key", Instant.now(), 1), StringKeyPair.KEYPAIR);
    }
}
