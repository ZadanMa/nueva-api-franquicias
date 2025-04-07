package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.ProductoEntity;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductoMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoPersistenceAdapterTest {

    @Mock
    private ProductoRepository repository;
    @Mock
    private ProductoMapper mapper;

    @InjectMocks
    private ProductoPersistenceAdapter adapter;

    @Test
    void testSave_Success() {
        Producto producto = new Producto(null, "Hamburguesa", 50, 101L);
        ProductoEntity entity = new ProductoEntity();
        entity.setNombre("Hamburguesa");
        entity.setStock(50);
        entity.setSucursalId(101L);
        ProductoEntity savedEntity = new ProductoEntity();
        savedEntity.setId(201L);
        savedEntity.setNombre("Hamburguesa");
        savedEntity.setStock(50);
        Producto savedModel = new Producto(201L, "Hamburguesa", 50, 101L);

        when(mapper.toEntity(producto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toModel(savedEntity)).thenReturn(savedModel);

        StepVerifier.create(adapter.save(producto))
                .expectNext(savedModel)
                .verifyComplete();
    }

    @Test
    void testExistsBySucursalIdAndNombre() {
        Long sucursalId = 101L;
        String nombre = "Hamburguesa";

        when(repository.existsBySucursalIdAndNombre(sucursalId, nombre)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsBySucursalIdAndNombre(sucursalId, nombre))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindById_Success() {

        Long productoId = 201L;
        ProductoEntity entity = new ProductoEntity();
        entity.setId(productoId);
        entity.setNombre("Hamburguesa");
        entity.setStock(50);
        entity.setSucursalId(101L);
        Producto model = new Producto(productoId, "Hamburguesa", 50, 101L);

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
        ProductoEntity entity = new ProductoEntity();
        entity.setId(201L);
        entity.setNombre("Hamburguesa");
        entity.setStock(50);
        entity.setSucursalId(101L);
        Producto model = new Producto(201L, "Hamburguesa", 50, 101L);

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        // Act & Assert
        StepVerifier.create(adapter.findAll())
                .expectNext(model)
                .verifyComplete();
    }

}