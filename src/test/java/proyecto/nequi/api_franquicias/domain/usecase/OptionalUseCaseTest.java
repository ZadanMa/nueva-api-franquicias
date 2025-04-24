package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePorts;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OptionalUseCaseTest {

    @Mock
    private FranchisePersistencePorts persistencePort;

    private OptionalUseCase optionalUseCase;

    @BeforeEach
    void setUp() {
        optionalUseCase = new OptionalUseCase(persistencePort);
    }

    @Test
    void testGetFranchiseWithDetails_Success() {
        FranchiseWithDetails franquicia = new FranchiseWithDetails(1L, "Burger King", List.of());
        when(persistencePort.findWithDetailsById(1L)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(optionalUseCase.getFranchiseWithDetails(1L))
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void testGetFranchiseWithDetails_TechnicalError() {
        when(persistencePort.findWithDetailsById(1L)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(optionalUseCase.getFranchiseWithDetails(1L))
                .expectError(TechnicalException.class)
                .verify();
    }
}