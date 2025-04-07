package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FranquiciaUseCaseTest {

    @Mock
    private FranquiciaPersistencePort persistencePort;

    private FranquiciaUseCase franquiciaUseCase;

    @BeforeEach
    void setUp() {
        franquiciaUseCase = new FranquiciaUseCase(persistencePort);
    }

    @Test
    void testUpdateFranquiciaName_Success() {
        Long franquiciaId = 1L;
        String newName = "Burger Queen";
        Franquicia existing = new Franquicia(franquiciaId, "Burger King");
        Franquicia updated = new Franquicia(franquiciaId, newName);

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.existsByName(newName)).thenReturn(Mono.just(false));
        when(persistencePort.updateName(franquiciaId, newName)).thenReturn(Mono.just(updated));

        StepVerifier.create(franquiciaUseCase.updateFranquiciaName(franquiciaId, newName))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void testUpdateFranquiciaName_FranquiciaNotFound() {
        Long franquiciaId = 999L;
        String newName = "Burger Queen";

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaUseCase.updateFranquiciaName(franquiciaId, newName))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testUpdateFranquiciaName_NameAlreadyExists() {
        Long franquiciaId = 1L;
        String newName = "Burger Queen";
        Franquicia existing = new Franquicia(franquiciaId, "Burger King");

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.existsByName(newName)).thenReturn(Mono.just(true));

        StepVerifier.create(franquiciaUseCase.updateFranquiciaName(franquiciaId, newName))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testUpdateFranquiciaName_TechnicalError() {
        Long franquiciaId = 1L;
        String newName = "Burger Queen";
        Franquicia existing = new Franquicia(franquiciaId, "Burger King");

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.existsByName(newName)).thenReturn(Mono.just(false));
        when(persistencePort.updateName(franquiciaId, newName)).thenReturn(Mono.error(new RuntimeException("Error de BD")));

        StepVerifier.create(franquiciaUseCase.updateFranquiciaName(franquiciaId, newName))
                .expectError(TechnicalException.class)
                .verify();
    }
}