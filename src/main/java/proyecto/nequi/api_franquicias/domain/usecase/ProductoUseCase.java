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
    public Mono<Producto> agregarProducto(Long sucursalId, Producto producto) {
        return persistencePort.existsBySucursalIdAndNombre(sucursalId, producto.nombre())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS)))
                .flatMap(exists -> {
                    Producto productoConSucursal = new Producto(
                            producto.id(),
                            producto.nombre(),
                            producto.stock(),
                            sucursalId
                    );
                    return persistencePort.save(productoConSucursal);
                });
    }

    @Override
    public Mono<Producto> asociarProductoASucursal(Long sucursalId, Long productoId) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .flatMap(producto -> persistencePort.asociarProductoASucursal(sucursalId, productoId)
                    .thenReturn(producto));
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
                .flatMap(producto -> persistencePort.updateNombre(productoId, nuevoNombre));
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