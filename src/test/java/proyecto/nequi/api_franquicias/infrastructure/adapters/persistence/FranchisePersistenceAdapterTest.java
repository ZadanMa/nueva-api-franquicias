package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.model.Franchise;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranchiseEntity;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranchiseMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranchiseRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FranchisePersistenceAdapterTest {

    @Mock
    private FranchiseRepository repository;
    @Mock
    private FranchiseMapper mapper;

    @InjectMocks
    private FranchisePersistenceAdapter adapter;



    @Test
    void testExistsByName() {
        String nombre = "Burger King";
        when(repository.existsByName(nombre)).thenReturn(Mono.just(true));


        StepVerifier.create(adapter.existsByName(nombre))
                .expectNext(true)
                .verifyComplete();
    }
    @Test
    void testFindById_Success() {
        Long id = 1L;
        Franchise model = new Franchise(id, "Burger King");
        FranchiseEntity entity = new FranchiseEntity();
        entity.setId(id);
        entity.setName("Burger King");


        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.findById(id))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testFindById_NotFound() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(id))
                .expectNextCount(0)
                .verifyComplete();
    }
    @Test
    void testUpdateName_Success() {
        Long id = 1L;
        String newName = "Burger Queen";
        FranchiseEntity entity = new FranchiseEntity();
        entity.setId(id);
        entity.setName("Burger King");

        FranchiseEntity updatedEntity = new FranchiseEntity();
        updatedEntity.setId(id);
        updatedEntity.setName(newName);
        Franchise updatedModel = new Franchise(id, newName);

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(updatedEntity));
        when(mapper.toModel(updatedEntity)).thenReturn(updatedModel);

        StepVerifier.create(adapter.updateName(id, newName))
                .expectNext(updatedModel)
                .verifyComplete();
    }
    @Test
    void testUpdateName_NotFound() {
        Long id = 999L;
        String newName = "Burger Queen";

        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.updateName(id, newName))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testDeleteById_NotFound() {
        Long id = 999L;

        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById(id))
                .expectNextCount(0)
                .verifyComplete();
    }

}