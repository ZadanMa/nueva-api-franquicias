package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.FranquiciaServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import reactor.core.publisher.Mono;


public class FranquiciaUseCase implements FranquiciaServicePort {

    private final FranquiciaPersistencePort persistencePort;

    public FranquiciaUseCase(FranquiciaPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Franquicia> registerFranquicia(Franquicia franquicia) {
        return persistencePort.existsByName(franquicia.nombre())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NAME_FOUND))
                        : persistencePort.save(franquicia))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY));
    }

    @Override
    public Mono<Franquicia> updateFranquiciaName(Long id, String newName) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)))
                .flatMap(existing -> persistencePort.existsByName(newName)
                        .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NAME_FOUND))
                                : persistencePort.updateName(id, newName)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }
}