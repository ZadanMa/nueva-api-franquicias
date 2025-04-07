package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.spi.FranquicePersistencePorts;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OptionalUseCaseTest {

    @Mock
    private FranquicePersistencePorts persistencePort;

    private OptionalUseCase optionalUseCase;

    @BeforeEach
    void setUp() {
        optionalUseCase = new OptionalUseCase(persistencePort);
    }

    @Test
    void testGetFranquiciaWithDetails_Success() {
        FranquiciaWithDetails franquicia = new FranquiciaWithDetails(1L, "Burger King", List.of());
        when(persistencePort.findWithDetailsById(1L)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(optionalUseCase.getFranquiciaWithDetails(1L))
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void testGetFranquiciaWithDetails_TechnicalError() {
        when(persistencePort.findWithDetailsById(1L)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(optionalUseCase.getFranquiciaWithDetails(1L))
                .expectError(TechnicalException.class)
                .verify();
    }
}