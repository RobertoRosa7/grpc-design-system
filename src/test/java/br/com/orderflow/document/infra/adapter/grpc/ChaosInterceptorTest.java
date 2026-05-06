package br.com.orderflow.document.infra.adapter.grpc;

import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.orderflow.document.infra.config.chaos.ChaosInterceptorConfig.ChaosInterceptor;
import br.com.orderflow.document.infra.config.chaos.ChaosInterceptorConfig.ChaosProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChaosInterceptorTest {

    @Mock
    private ServerCall<Object, Object> serverCall;

    @Mock
    private ServerCallHandler<Object, Object> handler;

    @Mock
    private MethodDescriptor<Object, Object> methodDescriptor;

    @Test
    void deveProsseguirNormalmente_quandoChaosDesabilitado() {
        ChaosProperties props = new ChaosProperties(
                false, 1.0, 0, 0, 1.0, 1.0, 1.0);
        ChaosInterceptor interceptor = new ChaosInterceptor(props);

        when(handler.startCall(any(), any())).thenReturn(new ServerCall.Listener<>() {
        });

        ServerCall.Listener<Object> listener = interceptor.interceptCall(serverCall, new Metadata(), handler);

        assertThat(listener).isNotNull();
        verify(serverCall, never()).close(any(), any());
    }

    @Test
    void deveRetornarUnavailable_quandoProbabilidadeDeFalhaSatisfeita() {
        // probability = 1.0 garante que qualquer roll < 1.0 dispara falha
        ChaosProperties props = new ChaosProperties(
                true, 0.0, 0, 0, 1.0, 0.0, 0.0);
        ChaosInterceptor interceptor = new ChaosInterceptor(props);
        when(serverCall.getMethodDescriptor()).thenReturn(methodDescriptor);
        when(methodDescriptor.getFullMethodName()).thenReturn("DocumentService/GeneratePdf");

        interceptor.interceptCall(serverCall, new Metadata(), handler);

        ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
        verify(serverCall).close(statusCaptor.capture(), any());
        assertThat(statusCaptor.getValue().getCode()).isEqualTo(Status.Code.UNAVAILABLE);
    }

    @Test
    void deveRetornarDeadlineExceeded_quandoTimeoutProbabilidadeSatisfeita() {
        // failure = 0, timeout = 1.0 → DEADLINE_EXCEEDED
        ChaosProperties props = new ChaosProperties(
                true, 0.0, 0, 0, 0.0, 1.0, 0.0);
        ChaosInterceptor interceptor = new ChaosInterceptor(props);
        when(serverCall.getMethodDescriptor()).thenReturn(methodDescriptor);
        when(methodDescriptor.getFullMethodName()).thenReturn("DocumentService/GeneratePdf");

        interceptor.interceptCall(serverCall, new Metadata(), handler);

        ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
        verify(serverCall).close(statusCaptor.capture(), any());
        assertThat(statusCaptor.getValue().getCode()).isEqualTo(Status.Code.DEADLINE_EXCEEDED);
    }
}
