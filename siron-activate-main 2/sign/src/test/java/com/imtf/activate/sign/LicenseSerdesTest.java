package com.imtf.activate.sign;

import com.imtf.activate.lib.SignedSironLicense;
import com.imtf.activate.lib.SironLicense;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LicenseSerdesTest {

    private static final String TEST_SIGNATURE = "ajGpNZcKtGhKO6gWtVPJGQy+3YOCH0Ec35rL4GlsrfKqk9mhNIieDPZCsuo7TgM6+b80qlyqZbjffiW/QTGZbOtoR8IPpRG+XNIjI4G7eO72boiVaBExo/rPIL3XM2Fbw/OuO1EkONsDquFnnoWaZQ0lCQ+yPh1DOBcDcrl/O4rED5dviHoJ3ejltXGT07jhGO/B8juhcTmRivh7cK3chrXfiyiwkXt5+C0jBEhCBamCdU4HNvhTnRpe5EEUz1DWcMrzHOKoLX5X3nPM2wqd6T6XumCeOzNBkmzfNUGnk1zHRKaOLmjxxX26zb3/+Y02tw+ebWBSGYOPBntDDqjOoA==";

    private final SironLicense testLicense = new SironLicense(
            List.of("acm"),
            "imtf",
            "IMTF",
            LocalDate.now(),
            LocalDate.of(2025, Month.MARCH, 22),
            UUID.randomUUID().toString()
    );

    @Test
    void serializeLic_deserializeLic_LicAreEquals() {
        LicenseSerdes serdes = new LicenseSerdes();
        String serializedLicense = serdes.serialize(testLicense);
        SironLicense deserializedLicense = serdes.deserializeLicense(serializedLicense);
        assertEquals(testLicense, deserializedLicense);
    }

    @Test
    void serializeSignedLic_deserializeSignedLic_LicAreEquals() {
        LicenseSerdes serdes = new LicenseSerdes();
        SignedSironLicense signedSironLicense = new SignedSironLicense(testLicense, TEST_SIGNATURE);
        String serializedSignedLicense = serdes.serializeSignedLicense(signedSironLicense);
        SignedSironLicense deserializedSignedLicense = serdes.deserializeSignedLicense(serializedSignedLicense);

        assertEquals(signedSironLicense, deserializedSignedLicense);
    }
}