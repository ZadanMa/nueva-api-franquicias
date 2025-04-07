package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.FranquiciaServicePort;
import proyecto.nequi.api_franquicias.domain.api.OptionalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranquiciaDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranquiciaWithDetailsDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.ErrorDTO;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Tag(name = "Franquicias", description = "Endpoints para gestión de franquicias")
public class FranquiciaHandler {

    private static Logger log = LoggerFactory.getLogger(FranquiciaHandler.class);

    private final FranquiciaServicePort servicePort;
    private final OptionalServicePort optionalServicePort;
    private final FranquiciaDTOMapper dtoMapper;
    private final FranquiciaWithDetailsDTOMapper detailsMapper;

    public FranquiciaHandler(
            FranquiciaServicePort servicePort,
            OptionalServicePort optionalServicePort,
            FranquiciaDTOMapper dtoMapper, FranquiciaWithDetailsDTOMapper detailsMapper) {
        this.servicePort = servicePort;
        this.optionalServicePort = optionalServicePort;
        this.dtoMapper = dtoMapper;
        this.detailsMapper = detailsMapper;

    }

    public Mono<ServerResponse> registerFranquicia(ServerRequest request) {
        return request.bodyToMono(FranquiciaDTO.class)
                .flatMap(dto -> servicePort.registerFranquicia(dtoMapper.toModel(dto))
                        .map(franquicia -> APIResponse.<FranquiciaDTO>builder()
                                .code(TechnicalMessage.FRANQUICIA_CREATED.getCode())
                                .message(TechnicalMessage.FRANQUICIA_CREATED.getMessage())
                                .data(dtoMapper.toDto(franquicia))
                                .build()
                        )
                )
                .flatMap(response -> ServerResponse.status(201).bodyValue(response))
                .onErrorResume(BusinessException.class, ex -> {
                    log.error("Error de negocio: {}", ex.getMessage());
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
                    log.error("Error técnico: {}", ex.getMessage());
                    return ServerResponse.status(500).bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .build()
                    );
                });
    }

    // Actualizar Nombre
    public Mono<ServerResponse> updateFranquiciaName(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(FranquiciaUpdateDTO.class)
                .flatMap(dto -> servicePort.updateFranquiciaName(id, dto.newName())
                        .map(franquicia -> APIResponse.<FranquiciaDTO>builder()
                                .code(TechnicalMessage.FRANQUICIA_UPDATED.getCode())
                                .message(TechnicalMessage.FRANQUICIA_UPDATED.getMessage())
                                .data(dtoMapper.toDto(franquicia))
                                .build()
                        )
                )
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

    public Mono<ServerResponse> getFranquiciaWithDetails(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return optionalServicePort.getFranquiciaWithDetails(id)
                .map(detailsMapper::toDto)
                .flatMap(dto -> ServerResponse.ok()
                        .bodyValue(APIResponse.<FranquiciaWithDetailsDTO>builder()
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
}