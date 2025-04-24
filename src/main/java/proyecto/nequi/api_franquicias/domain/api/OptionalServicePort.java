package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import reactor.core.publisher.Mono;

public interface OptionalServicePort {
    Mono<FranchiseWithDetails> getFranchiseWithDetails(Long id);
}
