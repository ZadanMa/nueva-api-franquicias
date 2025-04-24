package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Franchise;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchisePersistencePort persistencePort;

    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseUseCase = new FranchiseUseCase(persistencePort);
    }

    @Test
    void testUpdateFranchiseName_Success() {
        Long franquiciaId = 1L;
        String newName = "Burger Queen";
        Franchise existing = new Franchise(franquiciaId, "Burger King");
        Franchise updated = new Franchise(franquiciaId, newName);

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.existsByName(newName)).thenReturn(Mono.just(false));
        when(persistencePort.updateName(franquiciaId, newName)).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(franquiciaId, newName))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void testUpdateFranquiciaName_FranchiseNotFound() {
        Long franquiciaId = 999L;
        String newName = "Burger Queen";

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.updateFranchiseName(franquiciaId, newName))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testUpdateFranchiseName_NameAlreadyExists() {
        Long franquiciaId = 1L;
        String newName = "Burger Queen";
        Franchise existing = new Franchise(franquiciaId, "Burger King");

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.existsByName(newName)).thenReturn(Mono.just(true));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(franquiciaId, newName))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testUpdateFranchiseName_TechnicalError() {
        Long franquiciaId = 1L;
        String newName = "Burger Queen";
        Franchise existing = new Franchise(franquiciaId, "Burger King");

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.existsByName(newName)).thenReturn(Mono.just(false));
        when(persistencePort.updateName(franquiciaId, newName)).thenReturn(Mono.error(new RuntimeException("Error de BD")));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(franquiciaId, newName))
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void testDeleteFranquicia_FranchiseNotFound() {
        Long franquiciaId = 999L;

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.deleteFranchise(franquiciaId))
                .expectError(BusinessException.class)
                .verify();
    }
    @Test
    void testDeleteFranchise_TechnicalError() {
        Long franquiciaId = 1L;
        Franchise existing = new Franchise(franquiciaId, "Burger King");

        when(persistencePort.findById(franquiciaId)).thenReturn(Mono.just(existing));
        when(persistencePort.deleteById(franquiciaId)).thenReturn(Mono.error(new RuntimeException("Error de BD")));

        StepVerifier.create(franchiseUseCase.deleteFranchise(franquiciaId))
                .expectError(TechnicalException.class)
                .verify();
    }
}