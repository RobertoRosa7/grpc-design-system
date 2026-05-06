package br.com.orderflow.document.infra.adapter.grpc;

import br.com.orderflow.document.domain.dto.GenerateDocumentRequestDto;
import br.com.orderflow.document.domain.dto.GenerateDocumentResponseDto;
import br.com.orderflow.document.domain.dto.GetDocumentResponseDto;
import br.com.orderflow.document.domain.mapper.DocumentMapper;
import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.port.in.GeneratePdfPort;
import br.com.orderflow.document.domain.port.in.GetDocumentPort;
import br.com.orderflow.document.v1.DocumentServiceGrpc;
import br.com.orderflow.document.v1.GenerateDocumentRequest;
import br.com.orderflow.document.v1.GenerateDocumentResponse;
import br.com.orderflow.document.v1.GetDocumentRequest;
import br.com.orderflow.document.v1.GetDocumentResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Adaptador de entrada gRPC.
 * <p>
 * Responsável apenas por orquestrar: recebe a mensagem proto, delega ao
 * {@link DocumentGrpcMapper} para converter para DTO, ao {@link DocumentMapper}
 * para converter para modelo de domínio, executa o caso de uso e converte
 * o resultado de volta para proto.
 * Não contém lógica de negócio nem lógica de mapeamento.
 */
@GrpcService
public class DocumentGrpcAdapter extends DocumentServiceGrpc.DocumentServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(DocumentGrpcAdapter.class);

    private final GeneratePdfPort generatePdfUseCase;
    private final GetDocumentPort getDocumentUseCase;
    private final DocumentGrpcMapper grpcMapper;
    private final DocumentMapper documentMapper;

    public DocumentGrpcAdapter(
            GeneratePdfPort generatePdfUseCase,
            GetDocumentPort getDocumentUseCase,
            DocumentGrpcMapper grpcMapper,
            DocumentMapper documentMapper) {
        this.generatePdfUseCase = generatePdfUseCase;
        this.getDocumentUseCase = getDocumentUseCase;
        this.grpcMapper = grpcMapper;
        this.documentMapper = documentMapper;
    }

    @Override
    public void generateDocument(
            GenerateDocumentRequest request,
            StreamObserver<GenerateDocumentResponse> responseObserver) {

        log.info("gRPC GenerateDocument recebido [correlationId={}]", request.getCorrelationId());

        try {
            GenerateDocumentRequestDto requestDto = grpcMapper.toRequestDto(request);
            DocumentPayload payload = documentMapper.toPayload(requestDto);
            Document document = generatePdfUseCase.execute(payload);

            GenerateDocumentResponseDto responseDto = documentMapper.toGenerateResponse(document);
            GenerateDocumentResponse response = grpcMapper.toGenerateResponse(responseDto);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.warn("Requisição inválida: {}", e.getMessage());
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());

        } catch (Exception e) {
            log.error("Erro interno ao gerar documento", e);
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Erro interno ao processar documento").asRuntimeException());
        }
    }

    @Override
    public void getDocument(
            GetDocumentRequest request,
            StreamObserver<GetDocumentResponse> responseObserver) {

        log.debug("gRPC GetDocument recebido [documentId={}]", request.getDocumentId());

        Optional<Document> found = getDocumentUseCase.execute(request.getDocumentId());

        if (found.isEmpty()) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("Documento não encontrado: " + request.getDocumentId())
                            .asRuntimeException());
            return;
        }

        GetDocumentResponseDto responseDto = documentMapper.toGetResponse(found.get());
        GetDocumentResponse response = grpcMapper.toGetResponse(responseDto);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
