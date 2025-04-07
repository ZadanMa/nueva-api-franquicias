package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.domain.spi.ProductoPersistencePort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    @Mock
    private ProductoPersistencePort persistencePort;

    private ProductoUseCase productoUseCase;

    @BeforeEach
    void setUp() {
        productoUseCase = new ProductoUseCase(persistencePort);
    }

    @Test
    void registrarProducto_Success() {
        Producto producto = new Producto(null, "Hamburguesa", 50, 101L);
        Producto savedProducto = new Producto(201L, "Hamburguesa", 50, 101L);

        when(persistencePort.existsBySucursalIdAndNombre(101L, "Hamburguesa"))
                .thenReturn(Mono.just(false));
        when(persistencePort.save(producto)).thenReturn(Mono.just(savedProducto));

        StepVerifier.create(productoUseCase.registrarProducto(producto))
                .expectNext(savedProducto)
                .verifyComplete();
    }

    @Test
    void registrarProducto_AlreadyExists() {
        Producto producto = new Producto(null, "Hamburguesa", 50, 101L);

        when(persistencePort.existsBySucursalIdAndNombre(101L, "Hamburguesa"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(productoUseCase.registrarProducto(producto))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void getAllProductos_Success() {
        Producto producto1 = new Producto(1L, "Producto1", 10, 1L);
        Producto producto2 = new Producto(2L, "Producto2", 20, 1L);

        when(persistencePort.findAll()).thenReturn(Flux.just(producto1, producto2));

        StepVerifier.create(productoUseCase.getAllProductos())
                .expectNext(producto1, producto2)
                .verifyComplete();
    }

    @Test
    void getProductoById_NotFound() {
        when(persistencePort.findById(999L)).thenReturn(Mono.empty());


        StepVerifier.create(productoUseCase.getProductoById(999L))
                .expectError(BusinessException.class)
                .verify();
    }


    @Test
    void actualizarNombreProducto_NotFound() {
        Long productoId = 999L;
        String nuevoNombre = "Hamburguesa Vegana";

        when(persistencePort.findById(productoId)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.actualizarNombreProducto(productoId, nuevoNombre))
                .expectError(BusinessException.class)
                .verify();
    }
    @Test
    void actualizarNombreProducto_AlreadyExists() {
        Long productoId = 1L;
        String nuevoNombre = "ProductoExistente";
        Producto existingProducto = new Producto(productoId, "Producto1", 10, 1L);

        when(persistencePort.findById(productoId)).thenReturn(Mono.just(existingProducto));
        when(persistencePort.existsBySucursalIdAndNombre(existingProducto.sucursalId(), nuevoNombre)).thenReturn(Mono.just(true));

        StepVerifier.create(productoUseCase.actualizarNombreProducto(productoId, nuevoNombre))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getTechnicalMessage() == TechnicalMessage.PRODUCT_ALREADY_EXISTS)
                .verify();
    }


    @Test
    void modificarStockProducto_NegativeStock() {
        Long productoId = 201L;
        int nuevoStock = -10;

        Producto producto = new Producto(productoId, "Hamburguesa", 50, 101L);

        when(persistencePort.findById(productoId)).thenReturn(Mono.just(producto));

        StepVerifier.create(productoUseCase.modificarStockProducto(productoId, nuevoStock))
                .expectError(BusinessException.class)
                .verify();
    }
    @Test
    void modificarStockProducto_Success() {
        Long productoId = 1L;
        int nuevoStock = 20;
        Producto existingProducto = new Producto(productoId, "Producto1", 10, 1L);
        Producto updatedProducto = new Producto(productoId, "Producto1", nuevoStock, 1L);

        when(persistencePort.findById(productoId)).thenReturn(Mono.just(existingProducto));
        when(persistencePort.updateStock(productoId, nuevoStock)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.modificarStockProducto(productoId, nuevoStock))
                .expectNext(updatedProducto)
                .verifyComplete();
    }

    @Test
    void eliminarProducto_Success() {
        Long productoId = 201L;
        when(persistencePort.deleteById(productoId)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.eliminarProducto(productoId))
                .verifyComplete();
    }
}