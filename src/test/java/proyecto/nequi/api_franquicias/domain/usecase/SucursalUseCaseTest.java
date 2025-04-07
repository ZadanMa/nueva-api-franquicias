package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.domain.spi.SucursalPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Map;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalUseCaseTest {

    @Mock
    private SucursalPersistencePort persistencePort;

    private SucursalUseCase sucursalUseCase;

    @BeforeEach
    void setUp() {
        sucursalUseCase = new SucursalUseCase(persistencePort);
    }

    @Test
    void testRegistrarSucursal_Success() {
        // Arrange
        Sucursal sucursal = new Sucursal(null, "Sucursal A", 1L);
        Sucursal savedSucursal = new Sucursal(101L, "Sucursal A", 1L);

        when(persistencePort.existsByFranquiciaIdAndNombre(1L, "Sucursal A")).thenReturn(Mono.just(false));
        when(persistencePort.save(sucursal)).thenReturn(Mono.just(savedSucursal));

        StepVerifier.create(sucursalUseCase.registrarSucursal(sucursal))
                .expectNext(savedSucursal)
                .verifyComplete();
    }

    @Test
    void testRegistrarSucursal_AlreadyExists() {
        Sucursal sucursal = new Sucursal(null, "Sucursal A", 1L);

        when(persistencePort.existsByFranquiciaIdAndNombre(1L, "Sucursal A")).thenReturn(Mono.just(true));

        StepVerifier.create(sucursalUseCase.registrarSucursal(sucursal))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testGetSucursalById_Success() {

        Long sucursalId = 101L;
        Sucursal sucursal = new Sucursal(sucursalId, "Sucursal A", 1L);

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.just(sucursal));

        StepVerifier.create(sucursalUseCase.getSucursalById(sucursalId))
                .expectNext(sucursal)
                .verifyComplete();
    }

    @Test
    void testGetSucursalById_NotFound() {
        Long sucursalId = 999L;

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.empty());

        StepVerifier.create(sucursalUseCase.getSucursalById(sucursalId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testActualizarNombreSucursal_Success() {
        Long sucursalId = 101L;
        String nuevoNombre = "Sucursal Nueva";
        Sucursal existingSucursal = new Sucursal(sucursalId, "Sucursal Vieja", 1L);

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.just(existingSucursal));
        when(persistencePort.existsByFranquiciaIdAndNombre(1L, nuevoNombre)).thenReturn(Mono.just(false));
        when(persistencePort.updateNombre(sucursalId, nuevoNombre)).thenReturn(Mono.just(new Sucursal(sucursalId, nuevoNombre, 1L)));

        StepVerifier.create(sucursalUseCase.actualizarNombreSucursal(sucursalId, nuevoNombre))
                .expectNext(new Sucursal(sucursalId, nuevoNombre, 1L))
                .verifyComplete();
    }

    @Test
    void testActualizarNombreSucursal_AlreadyExists() {
        Long sucursalId = 101L;
        String nuevoNombre = "Sucursal Existente";
        Sucursal existingSucursal = new Sucursal(sucursalId, "Sucursal Vieja", 1L);

        when(persistencePort.findById(sucursalId)).thenReturn(Mono.just(existingSucursal));
        when(persistencePort.existsByFranquiciaIdAndNombre(1L, nuevoNombre)).thenReturn(Mono.just(true));

        StepVerifier.create(sucursalUseCase.actualizarNombreSucursal(sucursalId, nuevoNombre))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testProductoConMasStockPorSucursal_Success() {
        Long franquiciaId = 1L;
        Map<String, Object> productoData = Map.of(
                "producto_nombre", "Hamburguesa",
                "stock", 100,
                "sucursal_id", 101L
        );

        when(persistencePort.productoConMasStockPorSucursal(franquiciaId))
                .thenReturn(Flux.just(productoData));

        StepVerifier.create(sucursalUseCase.productoConMasStockPorSucursal(franquiciaId))
                .expectNext(productoData)
                .verifyComplete();
    }
}