package com.imtf.activate.sign;

import java.util.Optional;

import static com.imtf.activate.sign.Keys.PRIVATE_KEY_FILENAME;
import static com.imtf.activate.sign.Keys.PUBLIC_KEY_FILENAME;

/**
 * Utility class for converting string representation of a keypair in
 * a KeyPair object.
 */
final class StringKeyPair {

    private final String keypair;

    public StringKeyPair(String keypair) {
        this.keypair = clean(keypair);
    }

    public Optional<String> extractPrivateKey() {
        String[] keyValues = this.keypair.split(",");

        for (String keyValue : keyValues) {
            if (keyValue.trim().startsWith(PRIVATE_KEY_FILENAME)) {
                return Optional.of(keyValue.split(":")[1].trim());

            }
        }
        return Optional.empty();
    }

    public Optional<String> extractPublicKey() {
        String[] keyValues = this.keypair.split(",");

        for (String keyValue : keyValues) {
            if (keyValue.trim().startsWith(PUBLIC_KEY_FILENAME)) {
                return Optional.of(keyValue.split(":")[1].trim());
            }
        }
        return Optional.empty();
    }

    private String clean(String dirty) {
        return dirty.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .trim();
    }
}
