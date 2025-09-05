package com.imtf.activate.sign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

import static com.imtf.activate.lib.RsaSettings.FORMAT;

public final class Keys {

    static final String PUBLIC_KEY_FILENAME = "public.key";

    static final String PRIVATE_KEY_FILENAME = "private.key";

    private Keys() {
    }

    public static void write(KeyPair pair, Path path) throws IOException {
        write(pair.getPublic(), Path.of(path.toString(), PUBLIC_KEY_FILENAME));
        write(pair.getPrivate(), Path.of(path.toString(), PRIVATE_KEY_FILENAME));
    }

    public static void write(PrivateKey key, Path filename) throws IOException {
        byte[] privateKey = Base64.getEncoder().encode(key.getEncoded());
        Files.write(filename, privateKey);
    }

    public static void write(PublicKey key, Path filename) throws IOException {
        byte[] publicKey = Base64.getEncoder().encode(key.getEncoded());
        Files.write(filename, publicKey);
    }


    /**
     * Convert a string representation of the keypair in a KeyPair object.
     * The string representation is the following :
     * { "public.key": "cjkdjfkdfjkd", "private.key": "fjdkfjdk" }
     */
    public static KeyPair readKeyPair(String keypair) throws NoSuchAlgorithmException, InvalidKeySpecException {
        StringKeyPair stringKeyPair = new StringKeyPair(keypair);
        Optional<String> oStringPublicKey = stringKeyPair.extractPublicKey();
        Optional<String> oStringPrivateKey = stringKeyPair.extractPrivateKey();

        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        if (oStringPublicKey.isPresent()) {
            publicKey = generatePublicKey(oStringPublicKey.get());
        }
        if (oStringPrivateKey.isPresent()) {
            privateKey = generatePrivateKey(oStringPrivateKey.get());
        }

        return new KeyPair(publicKey, privateKey);
    }

    public static KeyPair readKeyPair(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final PublicKey publicKey = readPublicKey(Path.of(path.toString(), PUBLIC_KEY_FILENAME));
        final PrivateKey privateKey = readPrivateKey(Path.of(path.toString(), PRIVATE_KEY_FILENAME));
        return new KeyPair(publicKey, privateKey);
    }

    public static PrivateKey readPrivateKey(Path filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] base64Key = Files.readAllBytes(filename);
        return generatePrivateKey(base64Key);
    }

    public static PublicKey readPublicKey(Path filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] base64Key = Files.readAllBytes(filename);
        return generatePublicKey(base64Key);
    }

    private static PrivateKey generatePrivateKey(byte[] base64Key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = Base64.getDecoder().decode(base64Key);
        KeyFactory keyFactory = KeyFactory.getInstance(FORMAT);
        PKCS8EncodedKeySpec encodedPrivateKeySpec = new PKCS8EncodedKeySpec(key);
        return keyFactory.generatePrivate(encodedPrivateKeySpec);
    }

    private static PrivateKey generatePrivateKey(String base64Key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generatePrivateKey(base64Key.getBytes(StandardCharsets.UTF_8));
    }

    private static PublicKey generatePublicKey(byte[] base64Key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = Base64.getDecoder().decode(base64Key);
        KeyFactory keyFactory = KeyFactory.getInstance(FORMAT);
        X509EncodedKeySpec encodedPublicKeySpec = new X509EncodedKeySpec(key);
        return keyFactory.generatePublic(encodedPublicKeySpec);
    }

    private static PublicKey generatePublicKey(String base64Key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generatePublicKey(base64Key.getBytes(StandardCharsets.UTF_8));
    }

}
