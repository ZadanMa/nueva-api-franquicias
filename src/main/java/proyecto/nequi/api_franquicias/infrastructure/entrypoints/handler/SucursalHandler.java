package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.SucursalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.SucursalDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Tag(name = "Sucursal", description = "Gestión de Sucursales")
public class SucursalHandler {

    private final SucursalServicePort servicePort;
    private final SucursalDTOMapper dtoMapper;

    public SucursalHandler(SucursalServicePort servicePort, SucursalDTOMapper dtoMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
    }

    public Mono<ServerResponse> registrarSucursal(ServerRequest request) {
        return request.bodyToMono(SucursalDTO.class)
                .flatMap(dto -> servicePort.registrarSucursal(dtoMapper.toModel(dto))
                        .map(sucursal -> APIResponse.<SucursalDTO>builder()
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

    public Mono<ServerResponse> getAllSucursales(ServerRequest request) {
        return servicePort.getAllSucursales()
                .map(dtoMapper::toDto)
                .collectList()
                .flatMap(dtos -> ServerResponse.ok().bodyValue(
                        APIResponse.<List<SucursalDTO>>builder()
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

    public Mono<ServerResponse> getSucursalById(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return servicePort.getSucursalById(sucursalId)
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(
                        APIResponse.<SucursalDTO>builder()
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

    public Mono<ServerResponse> actualizarNombreSucursal(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return request.bodyToMono(SucursalUpdateDTO.class)
                .flatMap(dto -> servicePort.actualizarNombreSucursal(sucursalId, dto.nuevoNombre())
                        .map(sucursal -> APIResponse.<SucursalDTO>builder()
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

    public Mono<ServerResponse> productoConMasStockPorSucursal(ServerRequest request) {
        Long franquiciaId = Long.valueOf(request.pathVariable("franquiciaId"));
        return servicePort.productoConMasStockPorSucursal(franquiciaId)
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
