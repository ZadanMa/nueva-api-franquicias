package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.domain.spi.ProductoPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductoMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductoPersistenceAdapter implements ProductoPersistencePort {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    public ProductoPersistenceAdapter(ProductoRepository repository, ProductoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return repository.save(mapper.toEntity(producto))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsBySucursalIdAndNombre(Long sucursalId, String nombre) {
        return repository.existsBySucursalIdAndNombre(sucursalId, nombre);
    }

    @Override
    public Mono<Producto> findById(Long productoId) {
        return repository.findById(productoId)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long productoId) {
        return repository.deleteById(productoId);
    }

    @Override
    public Mono<Producto> updateStock(Long productoId, int nuevoStock) {
        return repository.updateStock(productoId, nuevoStock);
    }


    @Override
    public Flux<Producto> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<Producto> updateNombre(Long productoId, String nuevoNombre) {
        return repository.updateNombre(productoId, nuevoNombre);
    }
}