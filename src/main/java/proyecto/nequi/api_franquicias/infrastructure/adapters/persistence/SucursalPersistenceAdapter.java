package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.domain.spi.SucursalPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.SucursalMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.SucursalRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

@Component
public class SucursalPersistenceAdapter implements SucursalPersistencePort {

    private final SucursalRepository repository;
    private final SucursalMapper mapper;

    public SucursalPersistenceAdapter(SucursalRepository repository, SucursalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        return repository.save(mapper.toEntity(sucursal))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByFranquiciaIdAndNombre(Long franquiciaId, String nombre) {
        return repository.existsByFranquiciaIdAndNombre(franquiciaId, nombre);
    }

    @Override
    public Mono<Sucursal> findById(Long sucursalId) {
        return repository.findById(sucursalId)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Sucursal> updateNombre(Long sucursalId, String nuevoNombre) {
        return repository.findById(sucursalId)
                .flatMap(entity -> {
                    entity.setNombre(nuevoNombre);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }

    @Override
    public Flux<Sucursal> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<Sucursal> asociarSucursalAFranquicia(Long franquiciaId, Long sucursalId) {
        return repository.asociarSucursalAFranquicia(franquiciaId, sucursalId);
    }

    @Override
    public Flux<Map<String, Object>> productoConMasStockPorSucursal(Long franquiciaId) {
        return repository.productoConMasStockPorSucursal(franquiciaId);
    }
}