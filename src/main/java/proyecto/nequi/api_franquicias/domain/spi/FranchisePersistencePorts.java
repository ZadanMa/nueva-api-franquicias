package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePorts {
    Mono<FranchiseWithDetails> findWithDetailsById(Long id);
}
