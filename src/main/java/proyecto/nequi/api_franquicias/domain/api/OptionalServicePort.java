package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import reactor.core.publisher.Mono;

public interface OptionalServicePort {
    Mono<FranquiciaWithDetails> getFranquiciaWithDetails(Long id);
}
