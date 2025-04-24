package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {
    Mono<Product> save(Product product);
    Mono<Boolean> existsBySucursalIdAndNombre(Long branchId, String name);
    Mono<Product> findById(Long productId);
    Mono<Product> updateNombre(Long productId, String newName);
    Mono<Product> updateStock(Long productId, int newStock);
    Flux<Product> findAll();
    Mono<Void> deleteById(Long productId);
}