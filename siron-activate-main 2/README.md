# Siron Activate licensing management

Siron Activate is a collections of tools, services and apis used for managing licenses at IMTF.

## license-service

The *licenses-service* provides the following functionalities:

* Creation of licenses
* Signature of licenses
* Management of customer list
* Management of capabilities list

The *licenses-service* is implemented with the quarkus framework and use a MongoDB database for storing capabilities,
customers and licenses information. The key pair used for signing the licenses are stored in AWS secret manager and
accessed by the backend service through the AWS client API.

### How to build

Siron activate services and tools are build using the following maven command:

```
mvn clean install
```

Tests can be skipped:

```
mvn clean install -DskipTests -DskipITs
```

### How to run tests

Unit and integration tests can be executed with the following maven command:

```
mvn test
```

### Build the docker image

The docker image of the backend service is built with the following command:

```
docker buildx build -f Dockerfile --platform=linux/amd64 -t license-service:1.0.0 .
```

### Environment variables

A list of all environment variables exposed by the licenses backend service:

| Name                                | Description                                                                                  | Mandatory |
|-------------------------------------|----------------------------------------------------------------------------------------------|-----------|
| QUARKUS_MONGODB_CONNECTION-STRING   | Connection string used by the MongoDB client to connect to the database                      | default   |
| AWS_ACCESS_KEY_ID                   | AWS access key id for accessing the private key in the Secrete Manager                       | yes       |
| AWS_SECRET_ACCESS_KEY               | AWS secret access key for accessing the private key in the Secrete Manager                   | yes       |
| AWS_SESSION_TOKEN                   | AWS session token for accessing the private key in the Secrete Manager                       | yes       |
| QUARKUS_OIDC_AUTH_SERVER_URL        | OIDC server URL (e.g. keycloak)                                                              | yes       |
| QUARKUS_OIDC_CLIENT_ID              | OIDC client ID                                                                               | yes       |
| QUARKUS_OIDC_CREDENTIALS_SECRET     | OIDC secret                                                                                  | yes       |
| QUARKUS_LOG_LEVEL                   | Logging level of the backend service (INFO/DEBUG/WARN/OFF)                                   | default   |
| QUARKUS_HTTP_CORS                   |                                                                                              | no        |
| QUARKUS_HTTP_CORS_ORIGINS           |                                                                                              | no        |
| QUARKUS_HTTP_CORS_METHOD            |                                                                                              | no        |
| QUARKUS_PROFILE                     | Select the quarkus profile. Default is prod. To activate OpenTelemetry, use the otel profile | no        |
| QUARKUS_OTEL_EXPORTER_OTLP_ENDPOINT | Opentelemetry exporter endpoint URL                                                          | no        |

### Docker compose

The directory *deployments/docker* contains docker compose files allowing to run the backend service and the frontend
locally. **The mandatory environment variables must be set before executing docker compose**

To run the backend and its MongoDB database, execute the following docker compose command:

```
docker compose -f docker-compose.yaml up
```

To run the backend, its MongoDB database and the frontend, execute the following docker compose command:

```
docker compose -f docker-compose-fe.yaml up
```


### Telemetry

Siron Activate uses OpenTelemetry as instrumentation library to report metrics, logs and traces. To enable OpenTelemetry exporter,
you must use the **otel** quarkus profile:

```
QUARKUS_PROFILE=otel
```

Once activated, Siron Activate will export telemetry data on **http://localhost:4317**. This value can be adapted
to your environment with following environment variable.

```
QUARKUS_OTEL_EXPORTER_OTLP_ENDPOINT=http://my-otel.collector.imtf.com:4317
```
