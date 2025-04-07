package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.SucursalEntity;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.SucursalMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.SucursalRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Map;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SucursalPersistenceAdapterTest {

    @Mock
    private SucursalRepository repository;
    @Mock
    private SucursalMapper mapper;
    @InjectMocks
    private SucursalPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SucursalPersistenceAdapter(repository, mapper);
    }

    @Test
    void testSave_Success() {
        Sucursal model = new Sucursal(null, "Sucursal A", 1L);
        SucursalEntity entity = new SucursalEntity();
        entity.setNombre("Sucursal A");
        entity.setFranquiciaId(1L);
        SucursalEntity savedEntity = new SucursalEntity();
        savedEntity.setId(101L);
        savedEntity.setNombre("Sucursal A");
        savedEntity.setFranquiciaId(1L);
        Sucursal savedModel = new Sucursal(101L, "Sucursal A", 1L);

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toModel(savedEntity)).thenReturn(savedModel);

        StepVerifier.create(adapter.save(model))
                .expectNext(savedModel)
                .verifyComplete();
    }

    @Test
    void testExistsByFranquiciaIdAndNombre() {
        when(repository.existsByFranquiciaIdAndNombre(1L, "Sucursal A")).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByFranquiciaIdAndNombre(1L, "Sucursal A"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindById_Success() {
        SucursalEntity entity = new SucursalEntity();
        entity.setId(101L);
        entity.setNombre("Sucursal A");
        entity.setFranquiciaId(1L);
        Sucursal model = new Sucursal(101L, "Sucursal A", 1L);

        when(repository.findById(101L)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.findById(101L))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(999L))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testUpdateNombre_Success() {
        SucursalEntity entity = new SucursalEntity();
        entity.setId(101L);
        entity.setNombre("Sucursal A");
        entity.setFranquiciaId(1L);
        SucursalEntity updatedEntity = new SucursalEntity();
        updatedEntity.setId(101L);
        updatedEntity.setNombre("Sucursal Nueva");
        updatedEntity.setFranquiciaId(1L);
        Sucursal updatedModel = new Sucursal(101L, "Sucursal Nueva", 1L);

        when(repository.findById(101L)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(updatedEntity));
        when(mapper.toModel(updatedEntity)).thenReturn(updatedModel);

        StepVerifier.create(adapter.updateNombre(101L, "Sucursal Nueva"))
                .expectNext(updatedModel)
                .verifyComplete();
    }

    @Test
    void testFindAll_Success() {
        SucursalEntity entity = new SucursalEntity();
        entity.setId(101L);
        entity.setNombre("Sucursal A");
        entity.setFranquiciaId(1L);
        Sucursal model = new Sucursal(101L, "Sucursal A", 1L);

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.findAll())
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testProductoConMasStockPorSucursal_Success() {
        Map<String, Object> data = Map.of(
                "producto_nombre", "Hamburguesa",
                "stock", 100,
                "sucursal_id", 101L
        );

        when(repository.productoConMasStockPorSucursal(1L)).thenReturn(Flux.just(data));

        StepVerifier.create(adapter.productoConMasStockPorSucursal(1L))
                .expectNext(data)
                .verifyComplete();
    }
}
