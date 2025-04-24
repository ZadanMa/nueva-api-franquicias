package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.BranchEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Mono<Boolean> existsByFranchiseIdAndName(Long franchiseId, String name);
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);

    @Query("SELECT s.nombre AS sucursal, p.nombre AS producto, MAX(p.stock) AS stock " +
            "FROM sucursales s " +
            "JOIN productos p ON s.id = p.sucursal_id " +
            "WHERE s.franquicia_id = :franquiciaId " +
            "GROUP BY s.id")
    Flux<Map<String, Object>> productoConMasStockPorSucursal(@Param("franchiseId") Long franchiseId);
}

