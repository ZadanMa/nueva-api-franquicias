package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.ProductServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.ProductDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Tag(name = "Product", description = "Gestión de Productos")
public class ProductHandler {

    private final ProductServicePort servicePort;
    private final ProductDTOMapper dtoMapper;

    public ProductHandler(ProductServicePort servicePort, ProductDTOMapper dtoMapper) {
        this.servicePort = servicePort;
        this.dtoMapper = dtoMapper;
    }

    public Mono<ServerResponse> registerProduct(ServerRequest request) {
        return request.bodyToMono(ProductDTO.class)
                .flatMap(dto -> servicePort.registerProduct(dtoMapper.toModel(dto))
                        .map(producto -> APIResponse.<ProductDTO>builder()
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

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        return servicePort.getAllProducts()
                .map(dtoMapper::toDto)
                .collectList()
                .flatMap(dtos -> ServerResponse.ok().bodyValue(
                        APIResponse.<List<ProductDTO>>builder()
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

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return servicePort.getProductById(productoId)
                .map(dtoMapper::toDto)
                .flatMap(dto -> ServerResponse.ok().bodyValue(
                        APIResponse.<ProductDTO>builder()
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

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return servicePort.deleteProduct(productoId)
                .thenReturn(APIResponse.builder()
                        .code(TechnicalMessage.PRODUCTO_DELETED.getCode())
                        .message(TechnicalMessage.PRODUCTO_DELETED.getMessage())
                        .build()
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

    public Mono<ServerResponse> updateNameProduct(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ProductUpdateDTO.class)
                .flatMap(dto -> servicePort.updateNameProduct(productoId, dto.newName())
                        .map(producto -> APIResponse.<ProductDTO>builder()
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

    public Mono<ServerResponse> modifyStockProduct(ServerRequest request) {
        Long productoId = Long.valueOf(request.pathVariable("productoId"));
        return request.bodyToMono(ProductUpdateStockDTO.class)
                .flatMap(dto -> servicePort.modifyProductStock(productoId, dto.newStock())
                        .map(producto -> APIResponse.<ProductDTO>builder()
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
