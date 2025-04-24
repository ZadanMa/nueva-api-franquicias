package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BranchServicePort {
    Mono<Branch> registerBranch(Branch branch);
    Flux<Branch> getAllSucursales();
    Mono<Branch> getBranchById(Long sucursalId);
    Mono<Branch> updateNameBranch(Long sucursalId, String nuevoNombre);
    Flux<Map<String, Object>> productMostStockPerBranch(Long franquiciaId);
}