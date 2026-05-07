package br.com.orderflow.document.infra.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Propriedades de seguranca JWT para chamadas gRPC.
 */
@Component
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtSecurityProperties {

  private boolean enabled = true;
  private String certificatePath = "classpath:certs/jwt-public.pem";
  private String issuer;
  private String audience;
  private long clockSkewSeconds = 60;
  private List<String> unsecuredMethods = new ArrayList<>(List.of(
      "grpc.reflection.v1alpha.ServerReflection/ServerReflectionInfo",
      "grpc.reflection.v1.ServerReflection/ServerReflectionInfo",
      "grpc.health.v1.Health/Check",
      "grpc.health.v1.Health/Watch"));

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getCertificatePath() {
    return certificatePath;
  }

  public void setCertificatePath(String certificatePath) {
    this.certificatePath = certificatePath;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getAudience() {
    return audience;
  }

  public void setAudience(String audience) {
    this.audience = audience;
  }

  public long getClockSkewSeconds() {
    return clockSkewSeconds;
  }

  public void setClockSkewSeconds(long clockSkewSeconds) {
    this.clockSkewSeconds = clockSkewSeconds;
  }

  public List<String> getUnsecuredMethods() {
    return unsecuredMethods;
  }

  public void setUnsecuredMethods(List<String> unsecuredMethods) {
    this.unsecuredMethods = unsecuredMethods == null ? new ArrayList<>() : unsecuredMethods;
  }
}
