package com.imtf.activate.sign;

import com.imtf.activate.lib.SignedSironLicense;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public final class Licenses {
    private Licenses() {
    }

    public static void write(Path path, SignedSironLicense sironLicense) throws IOException {
        String serialize = new LicenseSerdes().serializeSignedLicense(sironLicense);
        Files.write(path, serialize.getBytes(StandardCharsets.UTF_8));
    }

    public static void writeAsBase64(Path path, SignedSironLicense sironLicense) throws IOException {
        String serialize = new LicenseSerdes().serializeSignedLicense(sironLicense);
        Files.write(path, Base64.getEncoder().encode(serialize.getBytes(StandardCharsets.UTF_8)));
    }

    public static String asBase64(SignedSironLicense sironLicense) {
        String serialize = new LicenseSerdes().serializeSignedLicense(sironLicense);
        return Base64.getEncoder().encodeToString(serialize.getBytes(StandardCharsets.UTF_8));
    }
}
