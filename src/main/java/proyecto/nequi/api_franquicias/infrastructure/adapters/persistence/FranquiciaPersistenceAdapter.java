package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranquiciaMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaPersistenceAdapter implements FranquiciaPersistencePort {

    private final FranquiciaRepository repository;
    private final FranquiciaMapper mapper;
    private final FranquiciaWithDetailsMapper detailsMapper;

    public FranquiciaPersistenceAdapter(FranquiciaRepository repository, FranquiciaMapper mapper, FranquiciaWithDetailsMapper detailsMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.detailsMapper = detailsMapper;
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
    public Mono<FranquiciaWithDetails> findWithDetailsById(Long id) {
        return repository.findWithDetailsById(id)
                .map(detailsMapper::toModel);
    }
}
