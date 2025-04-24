package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.domain.spi.ProductPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductPersistenceAdapter(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(mapper.toEntity(product))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsBySucursalIdAndNombre(Long sucursalId, String nombre) {
        return repository.existsByBranchIdAndName(sucursalId, nombre);
    }

    @Override
    public Mono<Product> findById(Long productoId) {
        return repository.findById(productoId)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long productoId) {
        return repository.deleteById(productoId);
    }

    @Override
    public Mono<Product> updateStock(Long productoId, int nuevoStock) {
        return repository.findById(productoId)
                .flatMap(entity -> {
                    entity.setStock(nuevoStock);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }


    @Override
    public Flux<Product> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<Product> updateNombre(Long productoId, String nuevoNombre) {
        return repository.findById(productoId)
                .flatMap(entity -> {
                    entity.setName(nuevoNombre);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }
}