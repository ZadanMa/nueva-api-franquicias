package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<Franchise> registerFranchise(Franchise franchise);
    Mono<Franchise> updateFranchiseName(Long id, String newName);
    Mono<Void> deleteFranchise(Long id);
}