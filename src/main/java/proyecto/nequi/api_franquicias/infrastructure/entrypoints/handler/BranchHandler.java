package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.BranchServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.BranchDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Tag(name = "Branch", description = "Gestión de Sucursales")
public class BranchHandler {

    private final BranchServicePort servicePort;
    private final BranchDTOMapper dtoMapper;

    public BranchHandler(BranchServicePort servicePort, BranchDTOMapper dtoMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
    }

    public Mono<ServerResponse> registerBranch(ServerRequest request) {
        return request.bodyToMono(BranchDTO.class)
                .flatMap(dto -> servicePort.registerBranch(dtoMapper.toModel(dto))
                        .map(sucursal -> APIResponse.<BranchDTO>builder()
                                .code(TechnicalMessage.SUCURSAL_CREATED.getCode())
                                .message(TechnicalMessage.SUCURSAL_CREATED.getMessage())
                                .data(dtoMapper.toDto(sucursal))
                                .build()
                        )
                )
                .flatMap(response -> ServerResponse.status(201).bodyValue(response))
                .onErrorResume(BusinessException.class, ex ->
                    ServerResponse.badRequest().bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .build()
                    ))
                .onErrorResume(TechnicalException.class, ex ->
                    ServerResponse.status(500).bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .build()
                    ));
    }

    public Mono<ServerResponse> getAllBranch(ServerRequest request) {
        return servicePort.getAllSucursales()
                .map(dtoMapper::toDto)
                .collectList()
                .flatMap(dtos -> ServerResponse.ok().bodyValue(
                        APIResponse.<List<BranchDTO>>builder()
                                .code(TechnicalMessage.SUCURSAL_FOUND.getCode())
                                .message(TechnicalMessage.SUCURSAL_FOUND.getMessage())
                                .data(dtos)
                                .build()
                ))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(
                        APIResponse.builder()
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

    public Mono<ServerResponse> getBranchById(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return servicePort.getBranchById(sucursalId)
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(
                        APIResponse.<BranchDTO>builder()
                                .code(TechnicalMessage.SUCURSAL_FOUND.getCode())
                                .message(TechnicalMessage.SUCURSAL_FOUND.getMessage())
                                .data(dto)
                                .build()
                ))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(
                        APIResponse.builder()
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

    public Mono<ServerResponse> updateNameBranch(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return request.bodyToMono(BranchUpdateDTO.class)
                .flatMap(dto -> servicePort.updateNameBranch(sucursalId, dto.newName())
                        .map(sucursal -> APIResponse.<BranchDTO>builder()
                                .code(TechnicalMessage.SUCURSAL_UPDATED.getCode())
                                .message(TechnicalMessage.SUCURSAL_UPDATED.getMessage())
                                .data(dtoMapper.toDto(sucursal))
                                .build()
                        )
                )
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.badRequest().bodyValue(
                        APIResponse.builder()
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

    public Mono<ServerResponse> productMostStockPerBranch(ServerRequest request) {
        Long franquiciaId = Long.valueOf(request.pathVariable("franquiciaId"));
        return servicePort.productMostStockPerBranch(franquiciaId)
                .collectList()
                .flatMap(data -> ServerResponse.ok().bodyValue(
                        APIResponse.<List<Map<String, Object>>>builder()
                                .code(TechnicalMessage.PRODUCTO_FOUND.getCode())
                                .message(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                                .data(data)
                                .build()
                ))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(
                        APIResponse.builder()
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
}
