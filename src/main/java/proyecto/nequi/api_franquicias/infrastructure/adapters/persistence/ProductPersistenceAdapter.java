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
    public Mono<Boolean> existsBySucursalIdAndNombre(Long branchId, String name) {
        return repository.existsByBranchIdAndName(branchId, name);
    }

    @Override
    public Mono<Product> findById(Long productId) {
        return repository.findById(productId)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long productId) {
        return repository.deleteById(productId);
    }

    @Override
    public Mono<Product> updateStock(Long productId, int newStock) {
        return repository.findById(productId)
                .flatMap(entity -> {
                    entity.setStock(newStock);
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
    public Mono<Product> updateNombre(Long productId, String newName) {
        return repository.findById(productId)
                .flatMap(entity -> {
                    entity.setName(newName);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }
}