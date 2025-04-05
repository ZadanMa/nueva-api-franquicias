package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.ProductoServicePort;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.ProductoDTOMapper;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

    private final ProductoServicePort servicePort;
    private final ProductoDTOMapper dtoMapper;

    public ProductoHandler(ProductoServicePort servicePort, ProductoDTOMapper dtoMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
    }

    public Mono<ServerResponse> registrarProducto(ServerRequest request) {
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(dto -> servicePort.registrarProducto(dtoMapper.toModel(dto)))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.status(201).bodyValue(dto))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.badRequest().bodyValue(ex.getMessage()));
    }


    public Mono<ServerResponse> getAllProductos(ServerRequest request) {
        return servicePort.getAllProductos()
                .map(dtoMapper::toDto)
                .collectList()
                .flatMap(dtos -> ServerResponse.ok().bodyValue(dtos));
    }

    public Mono<ServerResponse> getProductoById(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return servicePort.getProductoById(productoId)
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminarProducto(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return servicePort.eliminarProducto(productoId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> actualizarNombreProducto(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ProductoUpdateDTO.class)
                .flatMap(dto -> servicePort.actualizarNombreProducto(productoId, dto.nuevoNombre()))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> modificarStockProducto(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ProductoUpdateStockDTO.class)
                .flatMap(dto -> servicePort.modificarStockProducto(productoId, dto.nuevoStock()))
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .onErrorResume(BusinessException.class, ex -> ServerResponse.badRequest().bodyValue(ex.getMessage()));
    }
}