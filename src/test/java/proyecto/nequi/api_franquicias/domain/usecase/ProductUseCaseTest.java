package proyecto.nequi.api_franquicias.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.domain.spi.ProductPersistencePort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductPersistencePort persistencePort;

    private ProductUseCase productoUseCase;

    @BeforeEach
    void setUp() {
        productoUseCase = new ProductUseCase(persistencePort);
    }

    @Test
    void registerProduct_Success() {
        Product product = new Product(null, "Hamburguesa", 50, 101L);
        Product savedProduct = new Product(201L, "Hamburguesa", 50, 101L);

        when(persistencePort.existsBySucursalIdAndNombre(101L, "Hamburguesa"))
                .thenReturn(Mono.just(false));
        when(persistencePort.save(product)).thenReturn(Mono.just(savedProduct));

        StepVerifier.create(productoUseCase.registerProduct(product))
                .expectNext(savedProduct)
                .verifyComplete();
    }

    @Test
    void registerProduct_AlreadyExists() {
        Product product = new Product(null, "Hamburguesa", 50, 101L);

        when(persistencePort.existsBySucursalIdAndNombre(101L, "Hamburguesa"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(productoUseCase.registerProduct(product))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void getAllProducts_Success() {
        Product product1 = new Product(1L, "Producto1", 10, 1L);
        Product product2 = new Product(2L, "Producto2", 20, 1L);

        when(persistencePort.findAll()).thenReturn(Flux.just(product1, product2));

        StepVerifier.create(productoUseCase.getAllProducts())
                .expectNext(product1, product2)
                .verifyComplete();
    }

    @Test
    void getProductById_NotFound() {
        when(persistencePort.findById(999L)).thenReturn(Mono.empty());


        StepVerifier.create(productoUseCase.getProductById(999L))
                .expectError(BusinessException.class)
                .verify();
    }


    @Test
    void updateNameProduct_NotFound() {
        Long productoId = 999L;
        String nuevoNombre = "Hamburguesa Vegana";

        when(persistencePort.findById(productoId)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.updateNameProduct(productoId, nuevoNombre))
                .expectError(BusinessException.class)
                .verify();
    }
    @Test
    void updateNameProduct_AlreadyExists() {
        Long productoId = 1L;
        String nuevoNombre = "ProductoExistente";
        Product existingProduct = new Product(productoId, "Producto1", 10, 1L);

        when(persistencePort.findById(productoId)).thenReturn(Mono.just(existingProduct));
        when(persistencePort.existsBySucursalIdAndNombre(existingProduct.branchId(), nuevoNombre)).thenReturn(Mono.just(true));

        StepVerifier.create(productoUseCase.updateNameProduct(productoId, nuevoNombre))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getTechnicalMessage() == TechnicalMessage.PRODUCT_ALREADY_EXISTS)
                .verify();
    }


    @Test
    void modifyProductStockProducto_NegativeStock() {
        Long productoId = 201L;
        int nuevoStock = -10;

        Product product = new Product(productoId, "Hamburguesa", 50, 101L);

        when(persistencePort.findById(productoId)).thenReturn(Mono.just(product));

        StepVerifier.create(productoUseCase.modifyProductStock(productoId, nuevoStock))
                .expectError(BusinessException.class)
                .verify();
    }
    @Test
    void modifyProductStock_Success() {
        Long productoId = 1L;
        int nuevoStock = 20;
        Product existingProduct = new Product(productoId, "Producto1", 10, 1L);
        Product updatedProduct = new Product(productoId, "Producto1", nuevoStock, 1L);

        when(persistencePort.findById(productoId)).thenReturn(Mono.just(existingProduct));
        when(persistencePort.updateStock(productoId, nuevoStock)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.modifyProductStock(productoId, nuevoStock))
                .expectNext(updatedProduct)
                .verifyComplete();
    }

    @Test
    void deleteProduct_Success() {
        Long productoId = 201L;
        when(persistencePort.deleteById(productoId)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.deleteProduct(productoId))
                .verifyComplete();
    }
}