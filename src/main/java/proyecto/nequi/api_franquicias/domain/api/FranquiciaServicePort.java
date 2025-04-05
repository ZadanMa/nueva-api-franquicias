package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import reactor.core.publisher.Mono;

public interface FranquiciaServicePort {
    Mono<Franquicia> registerFranquicia(Franquicia franquicia);
    Mono<Franquicia> updateFranquicia(Long id, Franquicia franquicia);
    Mono<FranquiciaWithDetails> getFranquiciaWithDetails(Long id);
}