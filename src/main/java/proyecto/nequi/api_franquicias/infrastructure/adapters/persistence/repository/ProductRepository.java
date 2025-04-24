package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.ProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Mono<Boolean> existsByBranchIdAndName(Long sucursalId, String nombre);
    Flux<ProductEntity> findByBranchId(Long sucursalId);
    @Query("UPDATE productos SET stock = :nuevoStock WHERE id = :productoId")
    Mono<Product> updateStock(@Param("productoId") Long productoId, @Param("newStock") int nuevoStock);

    @Query("UPDATE productos SET nombre = :nuevoNombre WHERE id = :productoId")
    Mono<Product> updateNombre(@Param("productoId") Long productoId, @Param("newName") String nuevoNombre);
}