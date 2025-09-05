package com.imtf.activate.sign;

import com.fasterxml.jackson.databind.JsonNode;
import com.imtf.activate.lib.InvalidSironLicenseException;
import com.imtf.activate.lib.RsaSettings;
import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;

public class LicenseSigner {

    private final KeyPair keyPair;

    public LicenseSigner(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public SignedSironLicense sign(final SironLicense license) {
        LicenseSerdes serdes = new LicenseSerdes();
        String stringSironLicense = serdes.serialize(license);
        String licenseSignature = encryptBase64(keyPair.getPrivate(), stringSironLicense);
        return new SignedSironLicense(license, licenseSignature);
    }

    private byte[] encrypt(final PrivateKey privateKey, final String data) {
        try {
            final Cipher encryptCipher = Cipher.getInstance(RsaSettings.ALGORITHM);
            String dataHash = DigestUtils.sha256Hex(data);
            encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return encryptCipher.doFinal(dataHash.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new InvalidSironLicenseException(e);
        }
    }


    private String encryptBase64(final PrivateKey privateKey, final String data) {
        return Base64.getEncoder().encodeToString(encrypt(privateKey, data));
    }
}


