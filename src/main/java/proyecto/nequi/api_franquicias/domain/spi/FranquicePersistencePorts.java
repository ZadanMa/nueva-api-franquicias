package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import reactor.core.publisher.Mono;

public interface FranquicePersistencePorts {
    Mono<FranquiciaWithDetails> findWithDetailsById(Long id);
}
