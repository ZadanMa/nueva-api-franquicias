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
    public Mono<Product> getProductById(Long productId) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_RETRIEVE_ENTITY));
    }

    @Override
    public Mono<Void> deleteProduct(Long productId) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .then(persistencePort.deleteById(productId))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_DELETE_ENTITY));
    }

    @Override
    public Mono<Product> updateNameProduct(Long productId, String newName) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .flatMap(existing -> persistencePort.existsBySucursalIdAndNombre(existing.branchId(), newName)
                        .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS))
                                : persistencePort.updateNombre(productId, newName)
                                .thenReturn(new Product(
                                        existing.id(),
                                        newName,
                                        existing.stock(),
                                        existing.branchId()
                                ))))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }
    @Override
    public Mono<Product> modifyProductStock(Long productId, int newStock) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                .filter( __ -> newStock >= 0)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NEGATIVE_STOCK)))
                .flatMap(existing -> {
                    Product updated = new Product(
                            existing.id(),
                            existing.name(),
                            newStock,
                            existing.branchId()
                    );
                    return persistencePort.updateStock(productId, newStock).thenReturn(updated);
                })
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_STOCK));
    }
}