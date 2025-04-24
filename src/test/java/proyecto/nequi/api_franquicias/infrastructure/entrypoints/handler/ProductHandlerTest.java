package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.ProductServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.ProductDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.router.ProductRouter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductHandlerTest {

    @Mock
    private ProductServicePort servicePort;
    @Mock
    private ProductDTOMapper dtoMapper;
    @InjectMocks
    private ProductHandler handler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {

        ProductRouter productRouter = new ProductRouter();
        routerFunction = productRouter.productoRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    // 1. Test: Registrar Product Exitoso
    @Test
    void testRegisterProduct_Success() {
        // Arrange
        ProductDTO requestDto = new ProductDTO(null, "Hamburguesa", 50, 101L);
        Product product = new Product(201L, "Hamburguesa", 50, 101L);
        ProductDTO responseDto = new ProductDTO(201L, "Hamburguesa", 50, 101L);

        when(dtoMapper.toModel(requestDto)).thenReturn(product);
        when(servicePort.registerProduct(product)).thenReturn(Mono.just(product));
        when(dtoMapper.toDto(product)).thenReturn(responseDto);

        // Act & Assert
        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_CREATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_CREATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(201)
                .jsonPath("$.data.name").isEqualTo("Hamburguesa")
                .jsonPath("$.data.stock").isEqualTo(50);
    }

    @Test
    void testRegisterProduct_BusinessException() {
        ProductDTO requestDto = new ProductDTO(null, "Hamburguesa", 50, 101L);
        Product product = new Product(201L, "Hamburguesa", 50, 101L);

        when(dtoMapper.toModel(requestDto)).thenReturn(product);
        when(servicePort.registerProduct(product))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS)));

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_ALREADY_EXISTS.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_ALREADY_EXISTS.getMessage());
    }
    @Test
    void testRegisterProduct_TechnicalException() {
        // Arrange
        ProductDTO requestDto = new ProductDTO(null, "Hamburguesa", 50, 101L);
        Product product = new Product(201L, "Hamburguesa", 50, 101L);
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(dtoMapper.toModel(requestDto)).thenReturn(product);
        when(servicePort.registerProduct(product)).thenReturn(Mono.error(technicalException));

        // Act & Assert
        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetAllProducts_Success() {
        Product product1 = new Product(201L, "Hamburguesa", 50, 101L);
        Product product2 = new Product(202L, "Papas", 30, 101L);
        List<Product> products = List.of(product1, product2);

        when(servicePort.getAllProducts()).thenReturn(Flux.fromIterable(products));
        when(dtoMapper.toDto(any(Product.class))).thenReturn(new ProductDTO(201L, "Hamburguesa", 50, 101L));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[0].id").isEqualTo(201)
                .jsonPath("$.data[0].name").isEqualTo("Hamburguesa");
    }

    @Test
    void testGetAllProducts_TechnicalException() {
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.getAllProducts()).thenReturn(Flux.error(technicalException));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetProductById_Success() {
        Long productoId = 201L;
        Product product = new Product(productoId, "Hamburguesa", 50, 101L);
        ProductDTO responseDto = new ProductDTO(productoId, "Hamburguesa", 50, 101L);

        when(servicePort.getProductById(productoId)).thenReturn(Mono.just(product));
        when(dtoMapper.toDto(product)).thenReturn(responseDto);

        webTestClient.get().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                .jsonPath("$.data.id").isEqualTo(201)
                .jsonPath("$.data.name").isEqualTo("Hamburguesa");
    }


    @Test
    void testGetProductById_BusinessException() {

        Long productoId = 999L;
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.getProductById(productoId)).thenReturn(Mono.error(businessException));

        webTestClient.get().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testGetProductById_TechnicalException() {
        Long productoId = 999L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.getProductById(productoId)).thenReturn(Mono.error(technicalException));
        webTestClient.get().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        Long productoId = 201L;

        when(servicePort.deleteProduct(productoId)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_DELETED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_DELETED.getMessage());
    }
    @Test
    void testDeleteProduct_BusinessException() {
        Long productoId = 201L;
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.deleteProduct(productoId)).thenReturn(Mono.error(businessException));

        webTestClient.delete().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testDeleteProduct_TechnicalException() {

        Long productoId = 201L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.deleteProduct(productoId)).thenReturn(Mono.error(technicalException));

        webTestClient.delete().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testUpdateNameProduct_Success() {
        Long productoId = 201L;
        ProductUpdateDTO requestDto = new ProductUpdateDTO("Hamburguesa Vegana");
        Product productActualizado = new Product(productoId, "Hamburguesa Vegana", 50, 101L);
        ProductDTO responseDto = new ProductDTO(productoId, "Hamburguesa Vegana", 50, 101L);

        when(servicePort.updateNameProduct(productoId, requestDto.newName()))
                .thenReturn(Mono.just(productActualizado));
        when(dtoMapper.toDto(productActualizado)).thenReturn(responseDto);

        webTestClient.put().uri("/productos/{productoId}/nombre", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_UPDATED.getMessage())
                .jsonPath("$.data.name").isEqualTo("Hamburguesa Vegana");
    }
    @Test
    void testUpdateNameProduct_BusinessException() {

        Long productoId = 201L;
        ProductUpdateDTO requestDto = new ProductUpdateDTO("Hamburguesa Vegana");
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.updateNameProduct(productoId, requestDto.newName()))
                .thenReturn(Mono.error(businessException));

        webTestClient.put().uri("/productos/{productoId}/nombre", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testUpdateNameProduct_TechnicalException() {
        Long productoId = 201L;
        ProductUpdateDTO requestDto = new ProductUpdateDTO("Hamburguesa Vegana");
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.updateNameProduct(productoId, requestDto.newName()))
                .thenReturn(Mono.error(technicalException));

        webTestClient.put().uri("/productos/{productoId}/nombre", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testModifyStockProduct_Success() {
        Long productoId = 201L;
        ProductUpdateStockDTO requestDto = new ProductUpdateStockDTO(100);
        Product product = new Product(productoId, "Hamburguesa", 100, 101L);
        ProductDTO responseDto = new ProductDTO(productoId, "Hamburguesa", 100, 101L);

        when(servicePort.modifyProductStock(productoId, requestDto.newStock()))
                .thenReturn(Mono.just(product));
        when(dtoMapper.toDto(product)).thenReturn(responseDto);

        webTestClient.put().uri("/productos/{productoId}/stock", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_STOCK_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_STOCK_UPDATED.getMessage())
                .jsonPath("$.data.stock").isEqualTo(100);
    }

    @Test
    void testModifyStockProduct_BusinessException() {

        Long productoId = 201L;
        ProductUpdateStockDTO requestDto = new ProductUpdateStockDTO(-50);

        when(servicePort.modifyProductStock(productoId, -50))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.PRODUCTO_STOCK_INVALID)));


        webTestClient.put().uri("/productos/{productoId}/stock", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_STOCK_INVALID.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_STOCK_INVALID.getMessage());
    }
    @Test
    void testModifyStockProduct_TechnicalException() {
        Long productoId = 201L;
        ProductUpdateStockDTO requestDto = new ProductUpdateStockDTO(100);
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.modifyProductStock(productoId, requestDto.newStock()))
                .thenReturn(Mono.error(technicalException));

        webTestClient.put().uri("/productos/{productoId}/stock", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }
}