package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.domain.spi.BranchPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.BranchMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.BranchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

@Component
public class BranchPersistenceAdapter implements BranchPersistencePort {

    private final BranchRepository repository;
    private final BranchMapper mapper;

    public BranchPersistenceAdapter(BranchRepository repository, BranchMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return repository.save(mapper.toEntity(branch))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByFranquiciaIdAndNombre(Long franquiciaId, String nombre) {
        return repository.existsByFranchiseIdAndName(franquiciaId, nombre);
    }

    @Override
    public Mono<Branch> findById(Long branchId) {
        return repository.findById(branchId)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Branch> updateNombre(Long sucursalId, String nuevoNombre) {
        return repository.findById(sucursalId)
                .flatMap(entity -> {
                    entity.setName(nuevoNombre);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }

    @Override
    public Flux<Branch> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Flux<Map<String, Object>> productMostStockPerBranch(Long franquiciaId) {
        return repository.productoConMasStockPorSucursal(franquiciaId);
    }
}