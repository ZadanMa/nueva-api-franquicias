package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.ProductoEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepository extends ReactiveCrudRepository<ProductoEntity, Long> {
    Mono<Boolean> existsBySucursalIdAndNombre(Long sucursalId, String nombre);
    Mono<ProductoEntity> findById(Long productoId);
    Flux<ProductoEntity> findBySucursalId(Long sucursalId);
    @Query("UPDATE productos SET stock = :nuevoStock WHERE id = :productoId")
    Mono<Producto> updateStock(@Param("productoId") Long productoId, @Param("nuevoStock") int nuevoStock);

    @Query("UPDATE productos SET nombre = :nuevoNombre WHERE id = :productoId")
    Mono<Producto> updateNombre(@Param("productoId") Long productoId, @Param("nuevoNombre") String nuevoNombre);
}