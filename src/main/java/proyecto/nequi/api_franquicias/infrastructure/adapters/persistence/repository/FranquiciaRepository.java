package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranquiciaEntity;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository extends ReactiveCrudRepository<FranquiciaEntity, Long> {
    Mono<Boolean> existsByNombre(String nombre);
    Mono<FranquiciaEntity> findById(Long id);
    @Query("SELECT f.id AS franquicia_id, f.nombre AS franquicia_nombre, " +
            "s.id AS sucursal_id, s.nombre AS sucursal_nombre, " +
            "p.id AS producto_id, p.nombre AS producto_nombre, p.stock " +
            "FROM franquicias f " +
            "LEFT JOIN sucursales s ON f.id = s.franquicia_id " +
            "LEFT JOIN productos p ON s.id = p.sucursal_id " +
            "WHERE f.id = :id")
    Mono<FranquiciaWithDetails> findWithDetailsById(@Param("id") Long id);
}