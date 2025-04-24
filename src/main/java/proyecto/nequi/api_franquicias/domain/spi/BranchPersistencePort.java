package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BranchPersistencePort {
    Mono<Branch> save(Branch branch);
    Mono<Boolean> existsByFranchiseIdAndName(Long franchiseId, String name);
    Mono<Branch> findById(Long branchId);
    Mono<Branch> updateNombre(Long branchId, String newName);
    Flux<Branch> findAll();
    Flux<Map<String, Object>> productMostStockPerBranch(Long franchiseId);
}