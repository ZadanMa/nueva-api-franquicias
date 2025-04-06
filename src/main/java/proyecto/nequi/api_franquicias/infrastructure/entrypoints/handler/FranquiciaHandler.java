package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.FranquiciaServicePort;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranquiciaDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranquiciaDetailsMapper;
import reactor.core.publisher.Mono;

@Component
@Tag(name = "Franquicias", description = "Endpoints para gestión de franquicias")
public class FranquiciaHandler {

    private final FranquiciaServicePort servicePort;
    private final FranquiciaDTOMapper dtoMapper;
    private final FranquiciaDetailsMapper detailsMapper;

    public FranquiciaHandler(
            FranquiciaServicePort servicePort,
            FranquiciaDTOMapper dtoMapper,
            FranquiciaDetailsMapper detailsMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
        this.detailsMapper = detailsMapper;
    }

    public Mono<ServerResponse> registerFranquicia(ServerRequest request) {
        return request.bodyToMono(FranquiciaDTO.class)
                .flatMap(dto -> servicePort.registerFranquicia(dtoMapper.toModel(dto)))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.status(201).bodyValue(dto))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.badRequest().bodyValue(ex.getMessage()))
                .onErrorResume(TechnicalException.class, ex -> ServerResponse.status(500).bodyValue(ex.getMessage()));
    }

    public Mono<ServerResponse> updateFranquiciaName(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(FranquiciaUpdateDTO.class)
                .flatMap(dto -> servicePort.updateFranquiciaName(id, dto.newName()))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getFranquiciaWithDetails(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return servicePort.getFranquiciaWithDetails(id)
                .map(detailsMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.notFound().build());
    }
}