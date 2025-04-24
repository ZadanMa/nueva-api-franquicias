package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Franchise;

import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Boolean> existsByName(String nombre);
    Mono<Franchise> findById(Long id);
    Mono<Franchise> updateName(Long id, String newName);
    Mono<Void> deleteById(Long id);
}