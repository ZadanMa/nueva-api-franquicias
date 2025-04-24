package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.*;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranchiseMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranchiseRepository;

import reactor.core.publisher.Mono;

@Component
public class FranchisePersistenceAdapter implements FranchisePersistencePort {

    private final FranchiseRepository repository;
    private final FranchiseMapper mapper;



    public FranchisePersistenceAdapter(FranchiseRepository repository, FranchiseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(mapper.toEntity(franchise))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String nombre) {
        return repository.existsByName(nombre);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> updateName(Long id, String newName) {
        return repository.findById(id)
                .flatMap(entity -> {
                    entity.setName(newName);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

}