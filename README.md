<div align="center">

<img src="docs/ebook/assets/images/capa-template.svg" width="320" alt="Capa do livro gRPC com Spring Boot em Microsserviços" />

# gRPC com Spring Boot em Microsserviços
## Guia Completo do Zero a Produção

**Arquitetura Hexagonal · SOLID · Streaming · Segurança · Observabilidade · Deploy**

*Roberto Rosa da Silva — Guia Prático 2026*

</div>

---

## Sobre o livro

Este projeto é o repositório técnico de referência do livro **gRPC com Spring Boot em Microsserviços: Guia Completo do Zero a Produção**. O livro foi escrito para engenheiros e arquitetos Java que precisam construir sistemas distribuídos reais, com foco em prática, qualidade de código e operação confiável em produção.

A proposta central é oferecer um guia técnico progressivo que cobre todo o ciclo de vida de um serviço gRPC: do primeiro `.proto` ao deploy em Kubernetes, passando por arquitetura hexagonal, segurança JWT/mTLS, observabilidade com Micrometer e Prometheus, e resiliência com Resilience4j.

O projeto de referência concreto implementado ao longo do livro é o **`document-pdf-grpc`**: um microsserviço que gera PDFs sob demanda, armazena no MinIO e persiste metadados no MongoDB, exposto exclusivamente via gRPC.

---

## O que foi implementado neste repositório

### Infraestrutura base

- **Spring Boot 3.3.0** com Java 17 e Maven Wrapper
- **gRPC server** via `net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE`
- **Protocol Buffers 3.25.3** com geração de código via `protobuf-maven-plugin`
- **MongoDB** como banco de metadados via Spring Data
- **MinIO** como storage S3-compatible para os PDFs gerados
- **OpenPDF 1.3.39** para renderização dos documentos

### Arquitetura

- **Arquitetura Hexagonal (Ports and Adapters)** aplicada em todos os módulos
- Separação estrita entre `domain/`, `application/` e `infra/`
- Servidor gRPC como adaptador de entrada (`@GrpcService`)
- Adaptadores de saída isolados: `MinioStorageAdapter`, `DocumentMongoRepositoryAdapter`
- Caso de uso central: `GeneratePdfService` orquestrando renderização, storage e persistência

### Segurança

- Validação **JWT assimétrica com chave PEM** (`nimbus-jose-jwt:9.37.3`)
- Interceptor gRPC global: `JwtAuthServerInterceptor`
- Arquitetura de validação modular com padrão **policy/validation/constant**:
  - Policies: `ExpirationPolicy`, `NotBeforePolicy`, `SubjectPolicy`, `IssuerPolicy`, `AudiencePolicy`, `PemPublicKeyPolicy`
  - Validações: `JwtClaimsValidator`, `JwtSignatureValidator`, `JwtRolesExtractor`, `PemPublicKeyResolver`
- Métodos sem autenticação configuráveis via `application.yml` (health checks, reflection)

### Observabilidade

- **Spring Boot Actuator** com endpoints `/actuator/health`, `/actuator/prometheus` e `/actuator/metrics`
- **Micrometer + Prometheus** (`micrometer-registry-prometheus`) para métricas numéricas
- **`GrpcMetricsInterceptor`** — interceptor gRPC global que registra por método:
  - `grpc.server.requests.total` (Counter por método e status code)
  - `grpc.server.latency` (Timer com percentis p50/p95/p99)
- Tag `service: document-pdf-grpc` em todas as métricas para correlação no Grafana

### Resiliência

- **Resilience4j `2.2.0`** (`resilience4j-spring-boot3` + `resilience4j-micrometer`)
- `MinioStorageAdapter.store()` anotado com `@Retry(name="minio")` + `@CircuitBreaker(name="minio")`
- `DocumentMongoRepositoryAdapter.save()` e `findById()` com `@Retry(name="mongodb")` + `@CircuitBreaker(name="mongodb")`
- Configuração via `application.yml`: janela deslizante de 10 chamadas, 50% de threshold, 3 tentativas com backoff exponencial
- Fallbacks explícitos que relançam as exceções de domínio adequadas

### Testes e qualidade

- **ArchUnit 1.3.0** — testes de arquitetura em camadas (sem violação de dependência)
- Testes unitários no core de domínio sem dependências de framework
- Profile `chaos` para injeção de falhas gRPC (`UNAVAILABLE`, `DEADLINE_EXCEEDED`)
- 6 testes automatizados passando, BUILD SUCCESS validado com `.\mvnw.cmd test -DskipITs`

---

## Guia rápido do repositório

```
.
├── pom.xml                          # Build Maven, versões e dependências
├── src/
│   ├── main/
│   │   ├── proto/                   # Definições .proto (contrato gRPC)
│   │   ├── java/br/com/orderflow/document/
│   │   │   ├── domain/              # Core: modelos, portas e serviços de domínio
│   │   │   ├── application/         # Adaptadores de entrada (gRPC server)
│   │   │   └── infra/               # Adaptadores de saída + configs
│   │   │       ├── adapter/
│   │   │       │   ├── metrics/     # GrpcMetricsInterceptor (Micrometer)
│   │   │       │   ├── minio/       # MinioStorageAdapter + Resilience4j
│   │   │       │   ├── mongodb/     # DocumentMongoRepositoryAdapter + Resilience4j
│   │   │       │   └── security/    # JWT PEM interceptor e validações
│   │   │       └── config/          # Beans: MinioConfig, MongoDbConfig, MetricsConfig
│   │   └── resources/
│   │       ├── application.yml      # Config principal (gRPC, MongoDB, MinIO, Actuator, Resilience4j)
│   │       ├── application-chaos.yml # Profile de chaos engineering
│   │       └── certs/               # Certificado PEM para validação JWT
│   └── test/                        # ArchUnit + ChaosInterceptorTest
└── docs/
    └── ebook/
        ├── meta.md                  # Plano editorial
        └── manuscript/
            ├── frontmatter/         # Capa, sumário, prefácio, sobre o autor
            └── capitulos/           # 19 capítulos (cap01 a cap19)
```

### Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java (Corretto recomendado) | 17 |
| Maven Wrapper | incluído (`mvnw`) |
| MongoDB | 6.x |
| MinIO | última estável |
| Docker (opcional) | 24+ |

### Comandos principais

```bash
# compilar e gerar código protobuf
.\mvnw.cmd compile

# executar testes (sem integration tests)
.\mvnw.cmd test -DskipITs

# subir a aplicação localmente
.\mvnw.cmd spring-boot:run

# empacotar JAR
.\mvnw.cmd package -DskipTests
```

### Variáveis de ambiente principais

| Variável | Padrão (dev) | Descrição |
|---|---|---|
| `GRPC_PORT` | `9090` | Porta do servidor gRPC |
| `MONGODB_URI` | `mongodb://localhost:27017/orderflow` | URI de conexão com MongoDB |
| `MINIO_ENDPOINT` | `http://localhost:9000` | URL do MinIO |
| `MINIO_ACCESS_KEY` | `minioadmin` | Chave de acesso MinIO |
| `MINIO_SECRET_KEY` | `minioadmin` | Chave secreta MinIO |
| `MINIO_BUCKET` | `documents` | Nome do bucket de destino |
| `APP_SECURITY_JWT_ENABLED` | `true` | Habilita validação JWT |
| `APP_SECURITY_JWT_CERTIFICATE_PATH` | `classpath:certs/jwt-public.pem` | Certificado PEM público |

---

## Sumário do livro

### Parte I — Fundamentos

| Cap | Título |
|---|---|
| 1 | Introdução ao gRPC e RPC Moderno |
| 2 | Setup do Ecossistema Spring Boot + gRPC |
| 3 | Protocol Buffers na Prática |

### Parte II — Arquitetura e Design

| Cap | Título |
|---|---|
| 4 | Arquitetura Hexagonal Aplicada a Microsserviços Spring |
| 5 | Primeiro Serviço gRPC com Separação por Camadas Hexagonais |
| 6 | Contratos Robustos e Versionamento de APIs |
| 7 | Unary, Server Streaming, Client Streaming e Bidirectional Streaming |
| 8 | Interceptadores, Metadados, Deadlines, Retries e Tratamento de Erros |
| 9 | SOLID no Desenho de Use Cases, Portas e Adaptadores |

### Parte III — Plataforma e Operação

| Cap | Título |
|---|---|
| 10 | Segurança com TLS/mTLS, Autenticação e Autorização |
| 11 | Service Discovery, API Gateway e Coexistência REST + gRPC |
| 12 | Observabilidade: Logs, Métricas, Tracing (OpenTelemetry) |
| 13 | Resiliência: Circuit Breaker, Timeout, Backpressure e Idempotência |
| 14 | Testes: Unitários, Integração, Contrato e Performance |

### Parte IV — Entrega e Escala

| Cap | Título |
|---|---|
| 15 | Deploy em Containers e Kubernetes |
| 16 | Migração Gradual de REST para gRPC sem Quebrar o Domínio |
| 17 | Estudo de Caso Completo (Fim a Fim em Arquitetura Hexagonal) |
| 18 | Boas Práticas, Antipadrões e Checklist de Produção |
| 19 | Apêndices: referência de comandos, snippets e recursos |

---

## Autor

**Roberto Rosa da Silva**
Atuando na area de tecnologia desde 2014, com foco em arquitetura de software, Java, Spring Boot, gRPC e sistemas distribuidos em producao.

- GitHub: [RobertoRosa7](https://github.com/RobertoRosa7)
- LinkedIn: [roberto-rosa-da-silva](https://www.linkedin.com/in/roberto-rosa-da-silva-71911259/)
- Instagram: [@roberto.rosa.silva](https://www.instagram.com/roberto.rosa.silva/)
- Repositorio do livro: [RobertoRosa7/grpc-design-system](https://github.com/RobertoRosa7/grpc-design-system)
