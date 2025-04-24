package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.BranchEntity;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.BranchMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.BranchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Map;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BranchPersistenceAdapterTest {

    @Mock
    private BranchRepository repository;
    @Mock
    private BranchMapper mapper;
    @InjectMocks
    private BranchPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BranchPersistenceAdapter(repository, mapper);
    }

    @Test
    void testSave_Success() {
        Branch model = new Branch(null, "Branch A", 1L);
        BranchEntity entity = new BranchEntity();
        entity.setName("Branch A");
        entity.setFranchiseId(1L);
        BranchEntity savedEntity = new BranchEntity();
        savedEntity.setId(101L);
        savedEntity.setName("Branch A");
        savedEntity.setFranchiseId(1L);
        Branch savedModel = new Branch(101L, "Branch A", 1L);

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toModel(savedEntity)).thenReturn(savedModel);

        StepVerifier.create(adapter.save(model))
                .expectNext(savedModel)
                .verifyComplete();
    }

    @Test
    void testExistsByFranchiseIdAndName() {
        when(repository.existsByFranchiseIdAndName(1L, "Branch A")).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByFranchiseIdAndName(1L, "Branch A"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindById_Success() {
        BranchEntity entity = new BranchEntity();
        entity.setId(101L);
        entity.setName("Branch A");
        entity.setFranchiseId(1L);
        Branch model = new Branch(101L, "Branch A", 1L);

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
        BranchEntity entity = new BranchEntity();
        entity.setId(101L);
        entity.setName("Branch A");
        entity.setFranchiseId(1L);
        BranchEntity updatedEntity = new BranchEntity();
        updatedEntity.setId(101L);
        updatedEntity.setName("Branch Nueva");
        updatedEntity.setFranchiseId(1L);
        Branch updatedModel = new Branch(101L, "Branch Nueva", 1L);

        when(repository.findById(101L)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(updatedEntity));
        when(mapper.toModel(updatedEntity)).thenReturn(updatedModel);

        StepVerifier.create(adapter.updateNombre(101L, "Branch Nueva"))
                .expectNext(updatedModel)
                .verifyComplete();
    }

    @Test
    void testFindAll_Success() {
        BranchEntity entity = new BranchEntity();
        entity.setId(101L);
        entity.setName("Branch A");
        entity.setFranchiseId(1L);
        Branch model = new Branch(101L, "Branch A", 1L);

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.findAll())
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testProductMostStockPerBranch_Success() {
        Map<String, Object> data = Map.of(
                "producto_nombre", "Hamburguesa",
                "stock", 100,
                "sucursal_id", 101L
        );

        when(repository.productoConMasStockPorSucursal(1L)).thenReturn(Flux.just(data));

        StepVerifier.create(adapter.productMostStockPerBranch(1L))
                .expectNext(data)
                .verifyComplete();
    }
}
