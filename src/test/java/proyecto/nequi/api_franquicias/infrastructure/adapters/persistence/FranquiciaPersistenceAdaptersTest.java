package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductoMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductoRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.SucursalRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FranquiciaPersistenceAdaptersTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private ProductoMapper productoMapper;

    private FranquiciaPersistenceAdapters adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranquiciaPersistenceAdapters(
                franquiciaRepository,
                sucursalRepository,
                productoRepository,
                productoMapper
        );
    }


    @Test
    void testFindWithDetailsById_FranquiciaNotFound() {
        Long franquiciaId = 999L;

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findWithDetailsById(franquiciaId))
                .expectNextCount(0)
                .verifyComplete();
    }


    @Test
    void testFindWithDetailsById_TechnicalError() {
        Long franquiciaId = 1L;

        when(franquiciaRepository.findById(franquiciaId))
                .thenReturn(Mono.error(new RuntimeException("Error de base de datos")));

        StepVerifier.create(adapter.findWithDetailsById(franquiciaId))
                .expectError(RuntimeException.class)
                .verify();
    }
}