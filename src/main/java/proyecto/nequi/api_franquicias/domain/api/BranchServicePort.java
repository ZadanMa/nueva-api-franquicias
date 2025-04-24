package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BranchServicePort {
    Mono<Branch> registerBranch(Branch branch);
    Flux<Branch> getAllSucursales();
    Mono<Branch> getBranchById(Long branchId);
    Mono<Branch> updateNameBranch(Long branchId, String newName);
    Flux<Map<String, Object>> productMostStockPerBranch(Long franchiseId);
}