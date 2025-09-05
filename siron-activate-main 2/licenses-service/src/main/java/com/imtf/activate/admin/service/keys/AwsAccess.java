package com.imtf.activate.admin.service.keys;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AwsAccess implements KeyAccess {

    private static final String AWS_SECRET_MANAGER_SECRET_NAME = "siron-license-keys";

    private static final Region REGION = Region.of("eu-west-1");

    @Override
    public Key getKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {

        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(REGION)
                .build()) {

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(AWS_SECRET_MANAGER_SECRET_NAME)
                    .build();

            GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            String secret = getSecretValueResponse.secretString();
            String arn = getSecretValueResponse.arn();
            Instant creationDate = getSecretValueResponse.createdDate();
            String name = getSecretValueResponse.name();
            String versionedId = getSecretValueResponse.versionId();
            List<String> versionedStages = getSecretValueResponse.versionStages();

            return new Key(
                    new KeyDescription(UUID.fromString(versionedId), name, creationDate, 0),
                    secret
            );
        }
    }
}
