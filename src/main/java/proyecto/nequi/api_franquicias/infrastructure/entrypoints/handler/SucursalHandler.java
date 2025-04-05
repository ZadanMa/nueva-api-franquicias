package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.SucursalServicePort;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.SucursalDTOMapper;
import reactor.core.publisher.Mono;

@Component
public class SucursalHandler {

    private final SucursalServicePort servicePort;
    private final SucursalDTOMapper dtoMapper;

    public SucursalHandler(SucursalServicePort servicePort, SucursalDTOMapper dtoMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
    }

    public Mono<ServerResponse> registrarSucursal(ServerRequest request) {
        return request.bodyToMono(SucursalDTO.class)
                .flatMap(dto -> servicePort.registrarSucursal(dtoMapper.toModel(dto)))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.status(201).bodyValue(dto))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.badRequest().bodyValue(ex.getMessage()));
    }

    public Mono<ServerResponse> getAllSucursales(ServerRequest request) {
        return servicePort.getAllSucursales()
                .map(dtoMapper::toDto)
                .collectList()
                .flatMap(dtos -> ServerResponse.ok().bodyValue(dtos));
    }

    public Mono<ServerResponse> getSucursalById(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return servicePort.getSucursalById(sucursalId)
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> actualizarNombreSucursal(ServerRequest request) {
        Long sucursalId = Long.valueOf(request.pathVariable("sucursalId"));
        return request.bodyToMono(SucursalUpdateDTO.class)
                .flatMap(dto -> servicePort.actualizarNombreSucursal(sucursalId, dto.nuevoNombre()))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> productoConMasStockPorSucursal(ServerRequest request) {
        Long franquiciaId = Long.valueOf(request.pathVariable("franquiciaId"));
        return servicePort.productoConMasStockPorSucursal(franquiciaId)
                .collectList()
                .flatMap(data -> ServerResponse.ok().bodyValue(data));
    }
}