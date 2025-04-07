package proyecto.nequi.api_franquicias.domain.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proyecto.nequi.api_franquicias.domain.api.OptionalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.spi.FranquicePersistencePorts;
import reactor.core.publisher.Mono;

public class OptionalUseCase implements OptionalServicePort {


    private final FranquicePersistencePorts persistencePort;
    public OptionalUseCase(FranquicePersistencePorts persistencePort) {
        this.persistencePort = persistencePort;
    }

    private final Logger log = LoggerFactory.getLogger(OptionalUseCase.class);

    @Override
    public Mono<FranquiciaWithDetails> getFranquiciaWithDetails(Long id) {
        return persistencePort.findWithDetailsById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)))
                .onErrorMap(ex -> {
                    log.error("Error técnico al recuperar franquicia: {}", ex.getMessage());
                    return new TechnicalException(TechnicalMessage.FAILED_TO_RETRIEVE_ENTITY);
                });
    }
}
