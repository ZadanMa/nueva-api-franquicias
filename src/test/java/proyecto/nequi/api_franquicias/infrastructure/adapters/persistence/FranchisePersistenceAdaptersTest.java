package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranchiseRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.BranchRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FranchisePersistenceAdaptersTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    private FranchisePersistenceAdapters adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchisePersistenceAdapters(
                franchiseRepository,
                branchRepository,
                productRepository,
                productMapper
        );
    }


    @Test
    void testFindWithDetailsById_FranquiciaNotFound() {
        Long franquiciaId = 999L;

        when(franchiseRepository.findById(franquiciaId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findWithDetailsById(franquiciaId))
                .expectNextCount(0)
                .verifyComplete();
    }


    @Test
    void testFindWithDetailsById_TechnicalError() {
        Long franquiciaId = 1L;

        when(franchiseRepository.findById(franquiciaId))
                .thenReturn(Mono.error(new RuntimeException("Error de base de datos")));

        StepVerifier.create(adapter.findWithDetailsById(franquiciaId))
                .expectError(RuntimeException.class)
                .verify();
    }
}