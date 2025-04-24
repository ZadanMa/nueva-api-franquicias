package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.BranchServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.domain.spi.BranchPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class BranchUseCase implements BranchServicePort {

    private final BranchPersistencePort persistencePort;

    public BranchUseCase(BranchPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Branch> registerBranch(Branch branch) {
        return persistencePort.existsByFranquiciaIdAndNombre(branch.franchiseId(), branch.name())
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_ALREADY_EXISTS))
                        : persistencePort.save(branch))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY));
    }

    @Override
    public Flux<Branch> getAllSucursales() {
        return persistencePort.findAll();
    }

    @Override
    public Mono<Branch> getBranchById(Long sucursalId) {
        return persistencePort.findById(sucursalId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_FIND_ENTITY));
    }

    @Override
    public Mono<Branch> updateNameBranch(Long sucursalId, String nuevoNombre) {
        return persistencePort.findById(sucursalId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)))
                .flatMap(sucursal -> persistencePort.existsByFranquiciaIdAndNombre(sucursal.franchiseId(), nuevoNombre)
                        .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_ALREADY_EXISTS))
                                : persistencePort.updateNombre(sucursalId, nuevoNombre)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }

    @Override
    public Flux<Map<String, Object>> productMostStockPerBranch(Long franquiciaId) {
        return persistencePort.productMostStockPerBranch(franquiciaId);
    }
}