package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.ProductoServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.ProductoDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Tag(name = "Producto", description = "Gestión de Productos")
public class ProductoHandler {

    private final ProductoServicePort servicePort;
    private final ProductoDTOMapper dtoMapper;

    public ProductoHandler(ProductoServicePort servicePort, ProductoDTOMapper dtoMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
    }

    public Mono<ServerResponse> registrarProducto(ServerRequest request) {
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(dto -> servicePort.registrarProducto(dtoMapper.toModel(dto))
                        .map(producto -> APIResponse.<ProductoDTO>builder()
                                .code(TechnicalMessage.PRODUCTO_CREATED.getCode())
                                .message(TechnicalMessage.PRODUCTO_CREATED.getMessage())
                                .data(dtoMapper.toDto(producto))
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
                .onErrorResume(TechnicalException.class, ex -> ServerResponse.status(500).bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .build()
                    ));
    }

    public Mono<ServerResponse> getAllProductos(ServerRequest request) {
        return servicePort.getAllProductos()
                .map(dtoMapper::toDto)
                .collectList()
                .flatMap(dtos -> ServerResponse.ok().bodyValue(
                        APIResponse.<List<ProductoDTO>>builder()
                                .code(TechnicalMessage.PRODUCTO_FOUND.getCode())
                                .message(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                                .data(dtos)
                                .build()
                ))
                .onErrorResume(TechnicalException.class, ex -> ServerResponse.status(500).bodyValue(
                        APIResponse.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build()
                ));
    }

    public Mono<ServerResponse> getProductoById(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return servicePort.getProductoById(productoId)
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(
                        APIResponse.<ProductoDTO>builder()
                                .code(TechnicalMessage.PRODUCTO_FOUND.getCode())
                                .message(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                                .data(dto)
                                .build()
                ))
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

    public Mono<ServerResponse> eliminarProducto(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return servicePort.eliminarProducto(productoId)
                .thenReturn(APIResponse.builder()
                        .code(TechnicalMessage.PRODUCTO_DELETED.getCode())
                        .message(TechnicalMessage.PRODUCTO_DELETED.getMessage())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok().bodyValue(response)) // 200 con mensaje
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

    public Mono<ServerResponse> actualizarNombreProducto(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ProductoUpdateDTO.class)
                .flatMap(dto -> servicePort.actualizarNombreProducto(productoId, dto.nuevoNombre())
                        .map(producto -> APIResponse.<ProductoDTO>builder()
                                .code(TechnicalMessage.PRODUCTO_UPDATED.getCode())
                                .message(TechnicalMessage.PRODUCTO_UPDATED.getMessage())
                                .data(dtoMapper.toDto(producto))
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

    public Mono<ServerResponse> modificarStockProducto(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ProductoUpdateStockDTO.class)
                .flatMap(dto -> servicePort.modificarStockProducto(productoId, dto.nuevoStock())
                        .map(producto -> APIResponse.<ProductoDTO>builder()
                                .code(TechnicalMessage.PRODUCTO_STOCK_UPDATED.getCode())
                                .message(TechnicalMessage.PRODUCTO_STOCK_UPDATED.getMessage())
                                .data(dtoMapper.toDto(producto))
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
                .onErrorResume(TechnicalException.class, ex ->  ServerResponse.status(500).bodyValue(
                            APIResponse.builder()
                                    .code(ex.getTechnicalMessage().getCode())
                                    .message(ex.getTechnicalMessage().getMessage())
                                    .build()
                    ));
    }
}
