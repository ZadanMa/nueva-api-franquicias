package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Franquicia;

import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaPersistencePort {
    Mono<Franquicia> save(Franquicia franquicia);
    Mono<Boolean> existsByName(String nombre);
    Mono<Franquicia> findById(Long id);
    Mono<Franquicia> updateName(Long id, String newName);
//    Mono<FranquiciaWithDetails> findWithDetailsById(Long id);
}