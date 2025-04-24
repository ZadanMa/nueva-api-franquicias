package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.ProductEntity;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPersistenceAdapterTest {

    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductPersistenceAdapter adapter;

    @Test
    void testSave_Success() {
        Product product = new Product(null, "Hamburguesa", 50, 101L);
        ProductEntity entity = new ProductEntity();
        entity.setName("Hamburguesa");
        entity.setStock(50);
        entity.setBranchId(101L);
        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(201L);
        savedEntity.setName("Hamburguesa");
        savedEntity.setStock(50);
        Product savedModel = new Product(201L, "Hamburguesa", 50, 101L);

        when(mapper.toEntity(product)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toModel(savedEntity)).thenReturn(savedModel);

        StepVerifier.create(adapter.save(product))
                .expectNext(savedModel)
                .verifyComplete();
    }

    @Test
    void testExistsBySucursalIdAndNombre() {
        Long sucursalId = 101L;
        String nombre = "Hamburguesa";

        when(repository.existsByBranchIdAndName(sucursalId, nombre)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsBySucursalIdAndNombre(sucursalId, nombre))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindById_Success() {

        Long productoId = 201L;
        ProductEntity entity = new ProductEntity();
        entity.setId(productoId);
        entity.setName("Hamburguesa");
        entity.setStock(50);
        entity.setBranchId(101L);
        Product model = new Product(productoId, "Hamburguesa", 50, 101L);

        when(repository.findById(productoId)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);


        StepVerifier.create(adapter.findById(productoId))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testFindById_NotFound() {
        Long productoId = 999L;
        when(repository.findById(productoId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(productoId))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testDeleteById_Success() {
        Long productoId = 201L;
        when(repository.deleteById(productoId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById(productoId))
                .verifyComplete();
    }

    @Test
    void testFindAll_Success() {
        ProductEntity entity = new ProductEntity();
        entity.setId(201L);
        entity.setName("Hamburguesa");
        entity.setStock(50);
        entity.setBranchId(101L);
        Product model = new Product(201L, "Hamburguesa", 50, 101L);

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        // Act & Assert
        StepVerifier.create(adapter.findAll())
                .expectNext(model)
                .verifyComplete();
    }

}