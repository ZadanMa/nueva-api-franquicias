package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.SucursalEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface SucursalRepository extends ReactiveCrudRepository<SucursalEntity, Long> {
    Mono<Boolean> existsByFranquiciaIdAndNombre(Long franquiciaId, String nombre);
    @Query("UPDATE sucursales SET franquicia_id = :franquiciaId WHERE id = :sucursalId")
    Mono<Sucursal> asociarSucursalAFranquicia(@Param("franquiciaId") Long franquiciaId, @Param("sucursalId") Long sucursalId);

    @Query("SELECT s.nombre AS sucursal, p.nombre AS producto, MAX(p.stock) AS stock " +
            "FROM sucursales s " +
            "JOIN productos p ON s.id = p.sucursal_id " +
            "WHERE s.franquicia_id = :franquiciaId " +
            "GROUP BY s.id")
    Flux<Map<String, Object>> productoConMasStockPorSucursal(@Param("franquiciaId") Long franquiciaId);
}

