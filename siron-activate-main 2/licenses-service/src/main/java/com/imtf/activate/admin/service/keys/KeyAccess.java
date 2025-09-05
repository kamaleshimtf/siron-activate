package com.imtf.activate.admin.service.keys;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface KeyAccess {
    Key getKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException;
}