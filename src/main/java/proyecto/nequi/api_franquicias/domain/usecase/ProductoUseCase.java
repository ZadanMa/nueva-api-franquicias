package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.ProductoServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
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
        return persistencePort.save(producto);
    }

    @Override
    public Flux<Producto> getAllProductos() {
        return persistencePort.findAll();
    }

    @Override
    public Mono<Producto> getProductoById(Long productoId) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)));
    }

    @Override
    public Mono<Void> eliminarProducto(Long productoId) {
        return persistencePort.deleteById(productoId);
    }

    @Override
    public Mono<Producto> actualizarNombreProducto(Long productoId, String nuevoNombre) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .flatMap(existing -> {
                    Producto updated = new Producto(
                            existing.id(),
                            nuevoNombre,
                            existing.stock(),
                            existing.sucursalId()
                    );
                    return persistencePort.updateNombre(productoId, nuevoNombre).thenReturn(updated);
            });
    }

    @Override
    public Mono<Producto> modificarStockProducto(Long productoId, int nuevoStock) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .filter(producto -> nuevoStock >= 0)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NEGATIVE_STOCK)))
                .flatMap(producto -> persistencePort.updateStock(productoId, nuevoStock));
    }
}