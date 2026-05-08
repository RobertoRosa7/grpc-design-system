<div align="center">

<img src="docs/ebook/assets/images/capa-template.svg" width="320" alt="Book cover: gRPC with Spring Boot in Microservices" />

# gRPC with Spring Boot in Microservices

## Complete Guide from Zero to Production

**Hexagonal Architecture · SOLID · Streaming · Security · Observability · Deploy**

_Roberto Rosa da Silva — Practical Guide 2026_

</div>

---

## About the book

This project is the technical reference repository for the book **gRPC with Spring Boot in Microservices: Complete Guide from Zero to Production**. The book was written for Java engineers and architects who need to build real distributed systems, with a focus on practice, code quality, and reliable production operations.

The central proposition is to offer a progressive technical guide covering the full lifecycle of a gRPC service: from the first `.proto` to deploy on Kubernetes, including hexagonal architecture, JWT/mTLS security, observability with Micrometer and Prometheus, and resilience with Resilience4j.

The concrete reference project implemented throughout the book is **`document-pdf-grpc`**: a microservice that generates PDFs on demand, stores them in MinIO, and persists metadata in MongoDB, exposed exclusively via gRPC.

---

## What was implemented in this repository

### Base infrastructure

- **Spring Boot 3.3.0** with Java 17 and Maven Wrapper
- **gRPC server** via `net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE`
- **Protocol Buffers 3.25.3** with code generation via `protobuf-maven-plugin`
- **MongoDB** as metadata database via Spring Data
- **MinIO** as S3-compatible storage for generated PDFs
- **OpenPDF 1.3.39** for document rendering

### Architecture

- **Hexagonal Architecture (Ports and Adapters)** applied across all modules
- Strict separation between `domain/`, `application/`, and `infra/`
- gRPC server as inbound adapter (`@GrpcService`)
- Isolated outbound adapters: `MinioStorageAdapter`, `DocumentMongoRepositoryAdapter`
- Central use case: `GeneratePdfService` orchestrating rendering, storage, and persistence

### Security

- **Asymmetric JWT validation with PEM key** (`nimbus-jose-jwt:9.37.3`)
- Global gRPC interceptor: `JwtAuthServerInterceptor`
- Modular validation architecture following the **policy/validation/constant** pattern:
  - Policies: `ExpirationPolicy`, `NotBeforePolicy`, `SubjectPolicy`, `IssuerPolicy`, `AudiencePolicy`, `PemPublicKeyPolicy`
  - Validators: `JwtClaimsValidator`, `JwtSignatureValidator`, `JwtRolesExtractor`, `PemPublicKeyResolver`
- Methods without authentication configurable via `application.yml` (health checks, reflection)

### Observability

- **Spring Boot Actuator** with endpoints `/actuator/health`, `/actuator/prometheus`, and `/actuator/metrics`
- **Micrometer + Prometheus** (`micrometer-registry-prometheus`) for numeric metrics
- **`GrpcMetricsInterceptor`** — global gRPC interceptor that records per method:
  - `grpc.server.requests.total` (Counter by method and status code)
  - `grpc.server.latency` (Timer with p50/p95/p99 percentiles)
- Tag `service: document-pdf-grpc` on all metrics for Grafana correlation

### Resilience

- **Resilience4j `2.2.0`** (`resilience4j-spring-boot3` + `resilience4j-micrometer`)
- `MinioStorageAdapter.store()` annotated with `@Retry(name="minio")` + `@CircuitBreaker(name="minio")`
- `DocumentMongoRepositoryAdapter.save()` and `findById()` with `@Retry(name="mongodb")` + `@CircuitBreaker(name="mongodb")`
- Configuration via `application.yml`: sliding window of 10 calls, 50% threshold, 3 attempts with exponential backoff
- Explicit fallbacks that rethrow appropriate domain exceptions

### Tests and quality

- **ArchUnit 1.3.0** — layered architecture tests (no dependency violations)
- Unit tests in the domain core with no framework dependencies
- `chaos` profile for gRPC fault injection (`UNAVAILABLE`, `DEADLINE_EXCEEDED`)
- 6 automated tests passing, BUILD SUCCESS validated with `.\mvnw.cmd test -DskipITs`

---

## Repository quick guide

```
.
├── pom.xml                          # Maven build, versions and dependencies
├── tooling/
│   └── ebook-engine/                # Submodule: editorial engine (EPUB/PDF/HTML/PNG/JPG)
├── src/
│   ├── main/
│   │   ├── proto/                   # .proto definitions (gRPC contract)
│   │   ├── java/br/com/orderflow/document/
│   │   │   ├── domain/              # Core: models, ports and domain services
│   │   │   ├── application/         # Inbound adapters (gRPC server)
│   │   │   └── infra/               # Outbound adapters + configs
│   │   │       ├── adapter/
│   │   │       │   ├── metrics/     # GrpcMetricsInterceptor (Micrometer)
│   │   │       │   ├── minio/       # MinioStorageAdapter + Resilience4j
│   │   │       │   ├── mongodb/     # DocumentMongoRepositoryAdapter + Resilience4j
│   │   │       │   └── security/    # JWT PEM interceptor and validators
│   │   │       └── config/          # Beans: MinioConfig, MongoDbConfig, MetricsConfig
│   │   └── resources/
│   │       ├── application.yml      # Main config (gRPC, MongoDB, MinIO, Actuator, Resilience4j)
│   │       ├── application-chaos.yml # Chaos engineering profile
│   │       └── certs/               # PEM certificate for JWT validation
│   └── test/                        # ArchUnit + ChaosInterceptorTest
└── docs/
  └── ebook/                       # Submodule: private editorial content (manuscript + assets + templates)
```

### Required submodules

```bash
# after cloning the repository
git submodule update --init --recursive

# update to referenced commits of the current branch
git submodule update --remote docs/ebook tooling/ebook-engine
```

### Prerequisites

| Tool                        | Minimum version   |
| --------------------------- | ----------------- |
| Java (Corretto recommended) | 17                |
| Maven Wrapper               | included (`mvnw`) |
| MongoDB                     | 6.x               |
| MinIO                       | latest stable     |
| Docker (optional)           | 24+               |

### Main commands

```bash
# compile and generate protobuf code
.\mvnw.cmd compile

# run tests (without integration tests)
.\mvnw.cmd test -DskipITs

# run the application locally
.\mvnw.cmd spring-boot:run

# package JAR
.\mvnw.cmd package -DskipTests
```

### Main environment variables

### Decoupled editorial pipeline

```bash
# generate EPUB via the decoupled engine
node tooling/ebook-engine/build.js epub

# generate PDF
node tooling/ebook-engine/build.js pdf

# generate all outputs
node tooling/ebook-engine/build.js all
```

| Variable                            | Default (dev)                         | Description             |
| ----------------------------------- | ------------------------------------- | ----------------------- |
| `GRPC_PORT`                         | `9090`                                | gRPC server port        |
| `MONGODB_URI`                       | `mongodb://localhost:27017/orderflow` | MongoDB connection URI  |
| `MINIO_ENDPOINT`                    | `http://localhost:9000`               | MinIO URL               |
| `MINIO_ACCESS_KEY`                  | `minioadmin`                          | MinIO access key        |
| `MINIO_SECRET_KEY`                  | `minioadmin`                          | MinIO secret key        |
| `MINIO_BUCKET`                      | `documents`                           | Destination bucket name |
| `APP_SECURITY_JWT_ENABLED`          | `true`                                | Enables JWT validation  |
| `APP_SECURITY_JWT_CERTIFICATE_PATH` | `classpath:certs/jwt-public.pem`      | Public PEM certificate  |

---

## Table of contents

### Part I — Fundamentals

| Ch  | Title                                       |
| --- | ------------------------------------------- |
| 1   | Introduction to gRPC and Modern RPC         |
| 2   | Setting Up the Spring Boot + gRPC Ecosystem |
| 3   | Protocol Buffers in Practice                |

### Part II — Architecture and Design

| Ch  | Title                                                                  |
| --- | ---------------------------------------------------------------------- |
| 4   | Hexagonal Architecture Applied to Spring Microservices                 |
| 5   | First gRPC Service with Hexagonal Layer Separation                     |
| 6   | Robust Contracts and API Versioning                                    |
| 7   | Unary, Server Streaming, Client Streaming, and Bidirectional Streaming |
| 8   | Interceptors, Metadata, Deadlines, Retries, and Error Handling         |
| 9   | SOLID in Use Case, Port, and Adapter Design                            |

### Part III — Platform and Operations

| Ch  | Title                                                               |
| --- | ------------------------------------------------------------------- |
| 10  | Security with TLS/mTLS, Authentication, and Authorization           |
| 11  | Service Discovery, API Gateway, and REST + gRPC Coexistence         |
| 12  | Observability: Logs, Metrics, Tracing (OpenTelemetry)               |
| 13  | Resilience: Circuit Breaker, Timeout, Backpressure, and Idempotency |
| 14  | Testing: Unit, Integration, Contract, and Performance               |

### Part IV — Delivery and Scale

| Ch  | Title                                                           |
| --- | --------------------------------------------------------------- |
| 15  | Deploy with Containers and Kubernetes                           |
| 16  | Gradual Migration from REST to gRPC without Breaking the Domain |
| 17  | Complete Case Study (End-to-End in Hexagonal Architecture)      |
| 18  | Best Practices, Anti-patterns, and Production Checklist         |
| 19  | Appendices: command reference, snippets, and resources          |

---

## Author

**Roberto Rosa da Silva**
Working in technology since 2014, focused on software architecture, Java, Spring Boot, gRPC, and distributed systems in production.

- GitHub: [RobertoRosa7](https://github.com/RobertoRosa7)
- LinkedIn: [roberto-rosa-da-silva](https://www.linkedin.com/in/roberto-rosa-da-silva-71911259/)
- Instagram: [@roberto.rosa.silva](https://www.instagram.com/roberto.rosa.silva/)
- Book repository: [RobertoRosa7/grpc-design-system](https://github.com/RobertoRosa7/grpc-design-system)
