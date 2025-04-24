package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.ProductServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.domain.spi.ProductPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductUseCase implements ProductServicePort {

    private final ProductPersistencePort persistencePort;

    public ProductUseCase(ProductPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Product> registerProduct(Product product) {
        return persistencePort.existsBySucursalIdAndNombre(product.branchId(), product.name())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS))
                        : persistencePort.save(product))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY));
    }

    @Override
    public Flux<Product> getAllProducts() {
        return persistencePort.findAll();
    }

    @Override
    public Mono<Product> getProductById(Long productoId) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_RETRIEVE_ENTITY));
    }

    @Override
    public Mono<Void> deleteProduct(Long productoId) {

        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .flatMap(existing -> persistencePort.deleteById(productoId))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_DELETE_ENTITY));
    }

    @Override
    public Mono<Product> updateNameProduct(Long productoId, String nuevoNombre) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .flatMap(existing -> persistencePort.existsBySucursalIdAndNombre(existing.branchId(), nuevoNombre)
                        .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS))
                                : persistencePort.updateNombre(productoId, nuevoNombre)
                                .thenReturn(new Product(
                                        existing.id(),
                                        nuevoNombre,
                                        existing.stock(),
                                        existing.branchId()
                                ))))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }
    @Override
    public Mono<Product> modifyProductStock(Long productoId, int nuevoStock) {
        return persistencePort.findById(productoId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .filter(producto -> nuevoStock >= 0)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NEGATIVE_STOCK)))
                .flatMap(existing -> {
                    Product updated = new Product(
                            existing.id(),
                            existing.name(),
                            nuevoStock,
                            existing.branchId()
                    );
                    return persistencePort.updateStock(productoId, nuevoStock).thenReturn(updated);
                })
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_STOCK));
    }
}