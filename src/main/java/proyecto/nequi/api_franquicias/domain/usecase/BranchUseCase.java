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
        return persistencePort.existsByFranchiseIdAndName(branch.franchiseId(), branch.name())
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
    public Mono<Branch> getBranchById(Long branchId) {
        return persistencePort.findById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_FIND_ENTITY));
    }

    @Override
    public Mono<Branch> updateNameBranch(Long branchId, String newName) {
        return persistencePort.findById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)))
                .flatMap(sucursal -> persistencePort.existsByFranchiseIdAndName(sucursal.franchiseId(), newName)
                        .flatMap(exists -> exists
                                ? Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_ALREADY_EXISTS))
                                : persistencePort.updateNombre(branchId, newName)))
                .onErrorMap(e -> e instanceof BusinessException ? e : new TechnicalException(TechnicalMessage.FAILED_TO_UPDATE_NAME));
    }

    @Override
    public Flux<Map<String, Object>> productMostStockPerBranch(Long franchiseId) {
        return persistencePort.productMostStockPerBranch(franchiseId);
    }
}