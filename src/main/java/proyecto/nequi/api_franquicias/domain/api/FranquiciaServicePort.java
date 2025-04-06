package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import reactor.core.publisher.Mono;

public interface FranquiciaServicePort {
    Mono<Franquicia> registerFranquicia(Franquicia franquicia);
    Mono<Franquicia> updateFranquiciaName(Long id, String newName);
//    Mono<FranquiciaWithDetails> getFranquiciaWithDetails(Long id);
}