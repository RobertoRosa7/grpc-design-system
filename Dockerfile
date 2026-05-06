# ============================================================
# Dockerfile — document-pdf-grpc (Java 17 / Amazon Corretto)
# Multi-stage: builder (Maven oficial) → runtime (JRE headless)
# Boas práticas: non-root, minimal layers, sem credenciais hardcoded
# ============================================================

# ── ESTÁGIO 1: builder ──────────────────────────────────────
# Imagem oficial Maven + Amazon Corretto 17 — elimina necessidade de instalar Maven manualmente
FROM maven:3.9.6-amazoncorretto-17 AS builder

WORKDIR /workspace

# Copia apenas o pom.xml primeiro para cache de dependências
COPY pom.xml pom.xml
COPY .mvn/   .mvn/

# Baixa dependências sem compilar — maximiza cache de layer
RUN mvn dependency:go-offline -B --no-transfer-progress -Dmaven.test.skip=true

# Copia fontes e compila
COPY src/ src/
RUN mvn package -B --no-transfer-progress \
      -DskipTests \
      -Dmaven.test.skip=true

# ── ESTÁGIO 2: runtime ─────────────────────────────────────
# JRE-only Alpine — imagem final mais leve que o builder
FROM amazoncorretto:17-alpine AS runtime

# Atualiza pacotes de segurança e instala utilitários mínimos
RUN apk update && apk upgrade --no-cache \
    && apk add --no-cache curl tzdata \
    && rm -rf /var/cache/apk/*

# Configura timezone
ENV TZ=America/Sao_Paulo

# Cria usuário e grupo não-root (UID 1001 — fora do range de sistemas)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup -u 1001

WORKDIR /app

# Copia apenas o fat-jar gerado pelo builder
COPY --from=builder --chown=appuser:appgroup \
     /workspace/target/*.jar app.jar

# Diretório para logs e dados temporários com permissão de escrita
RUN mkdir -p /app/logs /app/tmp \
    && chown -R appuser:appgroup /app

# Muda para o usuário não-root antes de expor
USER appuser

# Portas: 9090 gRPC, 8080 actuator/HTTP
EXPOSE 9090 8080

# Variáveis de ambiente sem credenciais — injetar via orchestrador (docker-compose, K8s Secret)
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=50.0 \
                       -XX:MaxRAMPercentage=75.0 \
                       -XX:+UseContainerSupport \
                       -XX:+ExitOnOutOfMemoryError \
                       -Djava.security.egd=file:/dev/./urandom \
                       -Djava.io.tmpdir=/app/tmp" \
    SERVER_PORT=8080 \
    GRPC_SERVER_PORT=9090

# Variáveis com defaults apenas para desenvolvimento — SOBRESCREVER em produção via secret
# SPRING_DATA_MONGODB_URI — ex: mongodb://user:pass@host:27017/orderflow?authSource=admin
# MINIO_ENDPOINT         — ex: http://minio:9000
# MINIO_ACCESS_KEY       — injetar via Kubernetes Secret ou --env-file
# MINIO_SECRET_KEY       — injetar via Kubernetes Secret ou --env-file
# MINIO_BUCKET_NAME      — ex: documents

# Healthcheck via Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Entrypoint explícito — evita uso de shell intermediário (melhor sinal)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
