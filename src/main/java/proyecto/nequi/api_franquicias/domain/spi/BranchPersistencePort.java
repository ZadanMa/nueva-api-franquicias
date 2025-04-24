package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BranchPersistencePort {
    Mono<Branch> save(Branch branch);
    Mono<Boolean> existsByFranquiciaIdAndNombre(Long franquiciaId, String nombre);
    Mono<Branch> findById(Long sucursalId);
    Mono<Branch> updateNombre(Long sucursalId, String nuevoNombre);
    Flux<Branch> findAll();
    Flux<Map<String, Object>> productMostStockPerBranch(Long franquiciaId);
}