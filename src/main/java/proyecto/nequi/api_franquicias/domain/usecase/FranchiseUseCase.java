package proyecto.nequi.api_franquicias.domain.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proyecto.nequi.api_franquicias.domain.api.FranchiseServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Franchise;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePort;
import reactor.core.publisher.Mono;


public class FranchiseUseCase implements FranchiseServicePort {

    private final FranchisePersistencePort persistencePort;

    private final Logger log = LoggerFactory.getLogger(FranchiseUseCase.class);
    public FranchiseUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Franchise> registerFranchise(Franchise franchise) {

        return persistencePort.existsByName(franchise.name())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NAME_FOUND))
                        : persistencePort.save(franchise))
                .onErrorMap(ex -> {
                    log.error("Error técnico al recuperar franquicia: {}", ex.getMessage());
                    return new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY);
                });
    }

    @Override
    public Mono<Franchise> updateFranchiseName(Long id, String newName) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)))
                .then(persistencePort.existsByName(newName))
                .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NAME_FOUND))
                                : persistencePort.updateName(id, newName))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }
    @Override
    public Mono<Void> deleteFranchise(Long id) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)))
                .then(persistencePort.deleteById(id))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_DELETE_ENTITY));
    }
}