package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.FranquiciaServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
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
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_ALREADY_EXISTS)))
                .flatMap(exists -> persistencePort.save(franquicia));
    }
    @Override
    public Mono<Franquicia> updateFranquicia(Long id, Franquicia franquicia) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)))
                .flatMap(existing -> {
                    Franquicia updated = new Franquicia(id, franquicia.nombre());
                    return persistencePort.save(updated);
                });
    }

    @Override
    public Mono<FranquiciaWithDetails> getFranquiciaWithDetails(Long id) {
        return persistencePort.findWithDetailsById(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));
    }
}