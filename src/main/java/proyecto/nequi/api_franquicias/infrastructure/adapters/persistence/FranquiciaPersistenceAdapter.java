package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranquiciaMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
    public Mono<FranquiciaWithDetails> findWithDetailsById(Long id) {
        return repository.findFranquiciaDetailsById(id)
                .map(this::mapToDomainModel)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));
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


    private FranquiciaWithDetails mapToDomainModel(Map<String, Object> rawData) {
        return new FranquiciaWithDetails(
                (Long) rawData.get("franquicia_id"),
                (String) rawData.get("franquicia_nombre"),
                List.of(new SucursalWithProductos(
                        (Long) rawData.get("sucursal_id"),
                        (String) rawData.get("sucursal_nombre"),
                        List.of(new Producto(
                                (Long) rawData.get("producto_id"),
                                (String) rawData.get("producto_nombre"),
                                (Integer) rawData.get("stock"),
                                null
                        ))
                ))
        );
    }
}
