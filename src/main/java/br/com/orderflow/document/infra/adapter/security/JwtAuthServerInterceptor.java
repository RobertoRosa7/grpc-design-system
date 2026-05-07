package br.com.orderflow.document.infra.adapter.security;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.BEARER_PREFIX;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.CONTEXT_KEY_JWT_PRINCIPAL;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.CONTEXT_KEY_JWT_SUBJECT;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.HEADER_AUTHORIZATION;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_BEARER_REQUIRED;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.config.security.JwtSecurityProperties;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Interceptor global que autentica JWT no metadata gRPC.
 */
@Component
@GrpcGlobalServerInterceptor
public class JwtAuthServerInterceptor implements ServerInterceptor {

  public static final Context.Key<JwtPrincipal> JWT_PRINCIPAL_CTX_KEY =
    Context.key(CONTEXT_KEY_JWT_PRINCIPAL);
  public static final Context.Key<String> JWT_SUBJECT_CTX_KEY =
    Context.key(CONTEXT_KEY_JWT_SUBJECT);

  private static final Metadata.Key<String> AUTHORIZATION_KEY =
    Metadata.Key.of(HEADER_AUTHORIZATION, Metadata.ASCII_STRING_MARSHALLER);

  private final JwtPemVerifier jwtPemVerifier;
  private final JwtSecurityProperties properties;

  public JwtAuthServerInterceptor(JwtPemVerifier jwtPemVerifier, JwtSecurityProperties properties) {
    this.jwtPemVerifier = jwtPemVerifier;
    this.properties = properties;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {

    if (!properties.isEnabled()) {
      return next.startCall(call, headers);
    }

    String fullMethodName = call.getMethodDescriptor().getFullMethodName();
    Set<String> unsecuredMethods = Set.copyOf(properties.getUnsecuredMethods());
    if (unsecuredMethods.contains(fullMethodName)) {
      return next.startCall(call, headers);
    }

    String authorization = headers.get(AUTHORIZATION_KEY);
    if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
      call.close(Status.UNAUTHENTICATED.withDescription(MSG_BEARER_REQUIRED), new Metadata());
      return new ServerCall.Listener<>() {
      };
    }

    String token = authorization.substring(BEARER_PREFIX.length());
    try {
      JwtPrincipal principal = jwtPemVerifier.validate(token);
      Context context = Context.current()
          .withValue(JWT_PRINCIPAL_CTX_KEY, principal)
          .withValue(JWT_SUBJECT_CTX_KEY, principal.subject());
      return Contexts.interceptCall(context, call, headers, next);
    } catch (JwtAuthenticationException ex) {
      call.close(Status.UNAUTHENTICATED.withDescription(ex.getMessage()), new Metadata());
      return new ServerCall.Listener<>() {
      };
    }
  }
}
