package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.*;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranquiciaMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;

import reactor.core.publisher.Mono;

@Component
public class FranquiciaPersistenceAdapter implements FranquiciaPersistencePort {

    private final FranquiciaRepository repository;
    private final FranquiciaMapper mapper;



    public FranquiciaPersistenceAdapter(FranquiciaRepository repository, FranquiciaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return repository.save(mapper.toEntity(franquicia))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public Mono<Franquicia> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }



    @Override
    public Mono<Franquicia> updateName(Long id, String newName) {
        return repository.findById(id)
                .flatMap(entity -> {
                    entity.setNombre(newName);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }

}