package br.com.orderflow.document.infra.adapter.security.constant;

/**
 * Literais compartilhados do modulo de seguranca JWT/PEM.
 */
public final class JwtSecurityLiterals {

  private JwtSecurityLiterals() {
  }

  public static final String CLAIM_ROLES = "roles";

  public static final String CONTEXT_KEY_JWT_PRINCIPAL = "jwtPrincipal";
  public static final String CONTEXT_KEY_JWT_SUBJECT = "jwtSubject";

  public static final String HEADER_AUTHORIZATION = "authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  public static final String MSG_BEARER_REQUIRED = "Token JWT Bearer obrigatorio";
  public static final String MSG_JWT_VALIDATION_FAILED = "Falha ao validar JWT";
  public static final String MSG_PEM_NOT_FOUND_PREFIX = "Arquivo PEM nao encontrado: ";
  public static final String MSG_PEM_LOAD_FAILED = "Falha ao carregar chave publica PEM";
  public static final String MSG_SIGNATURE_INVALID = "Assinatura JWT invalida";
  public static final String MSG_SIGNATURE_VALIDATION_FAILED = "Falha ao validar assinatura JWT";
  public static final String MSG_PEM_CONVERSION_FAILED = "Falha ao converter PEM em chave publica";
  public static final String MSG_INVALID_PEM = "PEM invalido. Esperado CERTIFICATE ou PUBLIC KEY";
  public static final String MSG_SUBJECT_MISSING = "Token sem subject";
  public static final String MSG_INVALID_ISSUER = "Issuer invalido";
  public static final String MSG_TOKEN_NOT_YET_VALID = "Token ainda nao valido (nbf)";
  public static final String MSG_TOKEN_EXPIRED_OR_MISSING_EXP = "Token expirado ou sem claim exp";
  public static final String MSG_INVALID_AUDIENCE = "Audience invalida";

  public static final String CERTIFICATE_MARKER = "BEGIN CERTIFICATE";
  public static final String X509_TYPE = "X.509";
  public static final String PUBLIC_KEY_MARKER = "BEGIN PUBLIC KEY";
  public static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
  public static final String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
  public static final String WHITESPACE_REGEX = "\\s";
  public static final String EMPTY = "";
  public static final String ALGORITHM_RSA = "RSA";
  public static final String ALGORITHM_EC = "EC";
}