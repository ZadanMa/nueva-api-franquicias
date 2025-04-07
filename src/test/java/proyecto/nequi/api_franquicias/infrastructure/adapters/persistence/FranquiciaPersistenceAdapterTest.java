package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranquiciaEntity;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranquiciaMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FranquiciaPersistenceAdapterTest {

    @Mock
    private FranquiciaRepository repository;
    @Mock
    private FranquiciaMapper mapper;

    @InjectMocks
    private FranquiciaPersistenceAdapter adapter;



    @Test
    void testExistsByName() {
        String nombre = "Burger King";
        when(repository.existsByNombre(nombre)).thenReturn(Mono.just(true));


        StepVerifier.create(adapter.existsByName(nombre))
                .expectNext(true)
                .verifyComplete();
    }
    @Test
    void testFindById_Success() {
        Long id = 1L;
        Franquicia model = new Franquicia(id, "Burger King");
        FranquiciaEntity entity = new FranquiciaEntity();
        entity.setId(id);
        entity.setNombre("Burger King");


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
        FranquiciaEntity entity = new FranquiciaEntity();
        entity.setId(id);
        entity.setNombre("Burger King");

        FranquiciaEntity updatedEntity = new FranquiciaEntity();
        updatedEntity.setId(id);
        updatedEntity.setNombre(newName);
        Franquicia updatedModel = new Franquicia(id, newName);

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(updatedEntity));
        when(mapper.toModel(updatedEntity)).thenReturn(updatedModel);

        StepVerifier.create(adapter.updateName(id, newName))
                .expectNext(updatedModel)
                .verifyComplete();
    }

}