package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoPersistencePort {
    Mono<Producto> save(Producto producto);
    Mono<Boolean> existsBySucursalIdAndNombre(Long sucursalId, String nombre);
    Mono<Producto> findById(Long productoId);
    Mono<Producto> updateNombre(Long productoId, String nuevoNombre);
    Mono<Producto> updateStock(Long productoId, int nuevoStock);
    Flux<Producto> findAll();
    Mono<Void> deleteById(Long productoId);
}