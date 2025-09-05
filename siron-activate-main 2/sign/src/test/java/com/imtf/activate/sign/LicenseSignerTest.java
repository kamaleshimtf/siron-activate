package com.imtf.activate.sign;

import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

class LicenseSignerTest {

    private final SironLicense testLicense = new SironLicense(
            List.of("acm"),
            "imtf",
            "IMTF",
            LocalDate.now(),
            LocalDate.of(2025, Month.MARCH, 22),
            UUID.randomUUID().toString()
    );

    @Test
    void tetete() throws NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = new RsaKeyPair();
        LicenseSigner licenseSigner = new LicenseSigner(rsaKeyPair.generate());
        SignedSironLicense signedLicense = licenseSigner.sign(testLicense);
        
    }

}