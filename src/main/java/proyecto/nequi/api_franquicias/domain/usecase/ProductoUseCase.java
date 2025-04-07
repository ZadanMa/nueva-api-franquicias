package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.ProductoServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.domain.spi.ProductoPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductoUseCase implements ProductoServicePort {

    private final ProductoPersistencePort persistencePort;

    public ProductoUseCase(ProductoPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Producto> registrarProducto(Producto producto) {
        return persistencePort.existsBySucursalIdAndNombre(producto.sucursalId(), producto.nombre())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS))
                        : persistencePort.save(producto))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY));
    }

    @Override
    public Flux<Producto> getAllProductos() {
        return persistencePort.findAll();
    }

    @Override
    public Mono<Producto> getProductoById(Long productoId) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_RETRIEVE_ENTITY));
    }

    @Override
    public Mono<Void> eliminarProducto(Long productoId) {
        return persistencePort.deleteById(productoId);
    }

    @Override
    public Mono<Producto> actualizarNombreProducto(Long productoId, String nuevoNombre) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .flatMap(existing -> persistencePort.existsBySucursalIdAndNombre(existing.sucursalId(), nuevoNombre)
                        .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS))
                                : persistencePort.updateNombre(productoId, nuevoNombre)
                                .thenReturn(new Producto(
                                        existing.id(),
                                        nuevoNombre,
                                        existing.stock(),
                                        existing.sucursalId()
                                ))))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }
    @Override
    public Mono<Producto> modificarStockProducto(Long productoId, int nuevoStock) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .filter(producto -> nuevoStock >= 0)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NEGATIVE_STOCK)))
                .flatMap(existing -> {
                    Producto updated = new Producto(
                            existing.id(),
                            existing.nombre(),
                            nuevoStock,
                            existing.sucursalId()
                    );
                    return persistencePort.updateStock(productoId, nuevoStock).thenReturn(updated);
                })
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_STOCK));
    }
}