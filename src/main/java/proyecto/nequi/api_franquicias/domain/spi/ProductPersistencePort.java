package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {
    Mono<Product> save(Product product);
    Mono<Boolean> existsBySucursalIdAndNombre(Long sucursalId, String nombre);
    Mono<Product> findById(Long productoId);
    Mono<Product> updateNombre(Long productoId, String nuevoNombre);
    Mono<Product> updateStock(Long productoId, int nuevoStock);
    Flux<Product> findAll();
    Mono<Void> deleteById(Long productoId);
}