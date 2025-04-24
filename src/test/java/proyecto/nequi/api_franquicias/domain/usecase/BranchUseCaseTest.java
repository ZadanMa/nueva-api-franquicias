package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.domain.spi.BranchPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Map;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @Mock
    private BranchPersistencePort persistencePort;

    private BranchUseCase sucursalUseCase;

    @BeforeEach
    void setUp() {
        sucursalUseCase = new BranchUseCase(persistencePort);
    }

    @Test
    void testRegisterBranch_Success() {
        // Arrange
        Branch branch = new Branch(null, "Branch A", 1L);
        Branch savedBranch = new Branch(101L, "Branch A", 1L);

        when(persistencePort.existsByFranchiseIdAndName(1L, "Branch A")).thenReturn(Mono.just(false));
        when(persistencePort.save(branch)).thenReturn(Mono.just(savedBranch));

        StepVerifier.create(sucursalUseCase.registerBranch(branch))
                .expectNext(savedBranch)
                .verifyComplete();
    }

    @Test
    void testRegisterBranch_AlreadyExists() {
        Branch branch = new Branch(null, "Branch A", 1L);

        when(persistencePort.existsByFranchiseIdAndName(1L, "Branch A")).thenReturn(Mono.just(true));

        StepVerifier.create(sucursalUseCase.registerBranch(branch))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testGetBranchById_Success() {

        Long sucursalId = 101L;
        Branch branch = new Branch(sucursalId, "Branch A", 1L);

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.just(branch));

        StepVerifier.create(sucursalUseCase.getBranchById(sucursalId))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void testGetBranchById_NotFound() {
        Long sucursalId = 999L;

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.empty());

        StepVerifier.create(sucursalUseCase.getBranchById(sucursalId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testUpdateNameBranch_Success() {
        Long sucursalId = 101L;
        String nuevoNombre = "Branch Nueva";
        Branch existingBranch = new Branch(sucursalId, "Branch Vieja", 1L);

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.just(existingBranch));
        when(persistencePort.existsByFranchiseIdAndName(1L, nuevoNombre)).thenReturn(Mono.just(false));
        when(persistencePort.updateNombre(sucursalId, nuevoNombre)).thenReturn(Mono.just(new Branch(sucursalId, nuevoNombre, 1L)));

        StepVerifier.create(sucursalUseCase.updateNameBranch(sucursalId, nuevoNombre))
                .expectNext(new Branch(sucursalId, nuevoNombre, 1L))
                .verifyComplete();
    }

    @Test
    void testUpdateNameBranch_AlreadyExists() {
        Long sucursalId = 101L;
        String nuevoNombre = "Branch Existente";
        Branch existingBranch = new Branch(sucursalId, "Branch Vieja", 1L);

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.just(existingBranch));
        when(persistencePort.existsByFranchiseIdAndName(1L, nuevoNombre)).thenReturn(Mono.just(true));

        StepVerifier.create(sucursalUseCase.updateNameBranch(sucursalId, nuevoNombre))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testProductMostStockPerBranch_Success() {
        Long franquiciaId = 1L;
        Map<String, Object> productoData = Map.of(
                "producto_nombre", "Hamburguesa",
                "stock", 100,
                "sucursal_id", 101L
        );

        when(persistencePort.productMostStockPerBranch(franquiciaId))
                .thenReturn(Flux.just(productoData));

        StepVerifier.create(sucursalUseCase.productMostStockPerBranch(franquiciaId))
                .expectNext(productoData)
                .verifyComplete();
    }
}