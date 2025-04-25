package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.FranchiseServicePort;
import proyecto.nequi.api_franquicias.domain.api.OptionalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranchiseDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranchiseWithDetailsDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.ErrorDTO;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Tag(name = "Franquicias", description = "Endpoints para gestión de franquicias")
public class FranchiseHandler {

    private final FranchiseServicePort servicePort;
    private final OptionalServicePort optionalServicePort;
    private final FranchiseDTOMapper dtoMapper;
    private final FranchiseWithDetailsDTOMapper detailsMapper;

    public FranchiseHandler(
            FranchiseServicePort servicePort,
            OptionalServicePort optionalServicePort,
            FranchiseDTOMapper dtoMapper, FranchiseWithDetailsDTOMapper detailsMapper) {
        this.servicePort = servicePort;
        this.optionalServicePort = optionalServicePort;
        this.dtoMapper = dtoMapper;
        this.detailsMapper = detailsMapper;

    }

    public Mono<ServerResponse> registerFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(dto -> servicePort.registerFranchise(dtoMapper.toModel(dto))
                        .map(franquicia -> APIResponse.<FranchiseDTO>builder()
                                .code(TechnicalMessage.FRANQUICIA_CREATED.getCode())
                                .message(TechnicalMessage.FRANQUICIA_CREATED.getMessage())
                                .data(dtoMapper.toDto(franquicia))
                                .build()
                        )
                )
                .flatMap(response -> ServerResponse.status(201).bodyValue(response))
                .onErrorResume(BusinessException.class, ex -> {
                    return ServerResponse.badRequest().bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .errors(List.of(ErrorDTO.builder()
                                            .code(ex.getTechnicalMessage().getCode())
                                            .message(ex.getTechnicalMessage().getMessage())
                                            .build()))
                                    .build()
                    );
                })
                .onErrorResume(TechnicalException.class, ex -> {
                    return ServerResponse.status(500).bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .build()
                    );
                });
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::valueOf)
                .flatMap(id -> request.bodyToMono(FranchiseUpdateDTO.class)
                        .flatMap(dto -> servicePort.updateFranchiseName(id, dto.newName())
                                .map(franquicia -> APIResponse.<FranchiseDTO>builder()
                                        .code(TechnicalMessage.FRANQUICIA_UPDATED.getCode())
                                        .message(TechnicalMessage.FRANQUICIA_UPDATED.getMessage())
                                        .data(dtoMapper.toDto(franquicia))
                                        .build()
                                )
                        ))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                ))
                .onErrorResume(TechnicalException.class, ex -> ServerResponse.status(500).bodyValue(
                        APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                ));
    }

    public Mono<ServerResponse> getFranchiseWithDetails(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::valueOf)
                .flatMap(id -> optionalServicePort.getFranchiseWithDetails(id))
                .map(detailsMapper::toDto)
                .flatMap(dto -> ServerResponse.ok()
                        .bodyValue(APIResponse.<FranchiseWithDetailsDTO>builder()
                                .code(TechnicalMessage.FRANQUICIA_FOUND.getCode())
                                .message(TechnicalMessage.FRANQUICIA_FOUND.getMessage())
                                .data(dto)
                                .build()
                        ))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                        ))
                .onErrorResume(TechnicalException.class, ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                        ));
    }

    public Mono<ServerResponse> deleteFranchise(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::valueOf)
                .flatMap(id -> servicePort.deleteFranchise(id))
                .thenReturn(APIResponse.builder()
                        .code(TechnicalMessage.FRANQUICIA_DELETED.getCode())
                        .message(TechnicalMessage.FRANQUICIA_DELETED.getMessage())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                        ))
                .onErrorResume(TechnicalException.class, ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                        ));
    }
}