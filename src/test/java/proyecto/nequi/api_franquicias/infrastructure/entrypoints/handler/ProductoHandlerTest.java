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
import proyecto.nequi.api_franquicias.domain.api.ProductoServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.ProductoDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.router.ProductoRouter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoHandlerTest {

    @Mock
    private ProductoServicePort servicePort;
    @Mock
    private ProductoDTOMapper dtoMapper;
    @InjectMocks
    private ProductoHandler handler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {

        ProductoRouter productoRouter = new ProductoRouter();
        routerFunction = productoRouter.productoRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    // 1. Test: Registrar Producto Exitoso
    @Test
    void testRegistrarProducto_Success() {
        // Arrange
        ProductoDTO requestDto = new ProductoDTO(null, "Hamburguesa", 50, 101L);
        Producto producto = new Producto(201L, "Hamburguesa", 50, 101L);
        ProductoDTO responseDto = new ProductoDTO(201L, "Hamburguesa", 50, 101L);

        when(dtoMapper.toModel(requestDto)).thenReturn(producto);
        when(servicePort.registrarProducto(producto)).thenReturn(Mono.just(producto));
        when(dtoMapper.toDto(producto)).thenReturn(responseDto);

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
                .jsonPath("$.data.nombre").isEqualTo("Hamburguesa")
                .jsonPath("$.data.stock").isEqualTo(50);
    }

    @Test
    void testRegistrarProducto_BusinessException() {
        ProductoDTO requestDto = new ProductoDTO(null, "Hamburguesa", 50, 101L);
        Producto producto = new Producto(201L, "Hamburguesa", 50, 101L);

        when(dtoMapper.toModel(requestDto)).thenReturn(producto);
        when(servicePort.registrarProducto(producto))
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
    void testRegistrarProducto_TechnicalException() {
        // Arrange
        ProductoDTO requestDto = new ProductoDTO(null, "Hamburguesa", 50, 101L);
        Producto producto = new Producto(201L, "Hamburguesa", 50, 101L);
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(dtoMapper.toModel(requestDto)).thenReturn(producto);
        when(servicePort.registrarProducto(producto)).thenReturn(Mono.error(technicalException));

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
    void testGetAllProductos_Success() {
        Producto producto1 = new Producto(201L, "Hamburguesa", 50, 101L);
        Producto producto2 = new Producto(202L, "Papas", 30, 101L);
        List<Producto> productos = List.of(producto1, producto2);

        when(servicePort.getAllProductos()).thenReturn(Flux.fromIterable(productos));
        when(dtoMapper.toDto(any(Producto.class))).thenReturn(new ProductoDTO(201L, "Hamburguesa", 50, 101L));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[0].id").isEqualTo(201)
                .jsonPath("$.data[0].nombre").isEqualTo("Hamburguesa");
    }

    @Test
    void testGetAllProductos_TechnicalException() {
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.getAllProductos()).thenReturn(Flux.error(technicalException));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetProductoById_Success() {
        Long productoId = 201L;
        Producto producto = new Producto(productoId, "Hamburguesa", 50, 101L);
        ProductoDTO responseDto = new ProductoDTO(productoId, "Hamburguesa", 50, 101L);

        when(servicePort.getProductoById(productoId)).thenReturn(Mono.just(producto));
        when(dtoMapper.toDto(producto)).thenReturn(responseDto);

        webTestClient.get().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                .jsonPath("$.data.id").isEqualTo(201)
                .jsonPath("$.data.nombre").isEqualTo("Hamburguesa");
    }


    @Test
    void testGetProductoById_BusinessException() {

        Long productoId = 999L;
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.getProductoById(productoId)).thenReturn(Mono.error(businessException));

        webTestClient.get().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testGetProductoById_TechnicalException() {
        Long productoId = 999L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.getProductoById(productoId)).thenReturn(Mono.error(technicalException));
        webTestClient.get().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testEliminarProducto_Success() {
        Long productoId = 201L;

        when(servicePort.eliminarProducto(productoId)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_DELETED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_DELETED.getMessage());
    }
    @Test
    void testEliminarProducto_BusinessException() {
        Long productoId = 201L;
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.eliminarProducto(productoId)).thenReturn(Mono.error(businessException));

        webTestClient.delete().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testEliminarProducto_TechnicalException() {

        Long productoId = 201L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.eliminarProducto(productoId)).thenReturn(Mono.error(technicalException));

        webTestClient.delete().uri("/productos/{productoId}", productoId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testActualizarNombreProducto_Success() {
        Long productoId = 201L;
        ProductoUpdateDTO requestDto = new ProductoUpdateDTO("Hamburguesa Vegana");
        Producto productoActualizado = new Producto(productoId, "Hamburguesa Vegana", 50, 101L);
        ProductoDTO responseDto = new ProductoDTO(productoId, "Hamburguesa Vegana", 50, 101L);

        when(servicePort.actualizarNombreProducto(productoId, requestDto.nuevoNombre()))
                .thenReturn(Mono.just(productoActualizado));
        when(dtoMapper.toDto(productoActualizado)).thenReturn(responseDto);

        webTestClient.put().uri("/productos/{productoId}/nombre", productoId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_UPDATED.getMessage())
                .jsonPath("$.data.nombre").isEqualTo("Hamburguesa Vegana");
    }
    @Test
    void testActualizarNombreProducto_BusinessException() {

        Long productoId = 201L;
        ProductoUpdateDTO requestDto = new ProductoUpdateDTO("Hamburguesa Vegana");
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.actualizarNombreProducto(productoId, requestDto.nuevoNombre()))
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
    void testActualizarNombreProducto_TechnicalException() {
        Long productoId = 201L;
        ProductoUpdateDTO requestDto = new ProductoUpdateDTO("Hamburguesa Vegana");
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.actualizarNombreProducto(productoId, requestDto.nuevoNombre()))
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
    void testModificarStockProducto_Success() {
        Long productoId = 201L;
        ProductoUpdateStockDTO requestDto = new ProductoUpdateStockDTO(100);
        Producto producto = new Producto(productoId, "Hamburguesa", 100, 101L);
        ProductoDTO responseDto = new ProductoDTO(productoId, "Hamburguesa", 100, 101L);

        when(servicePort.modificarStockProducto(productoId, requestDto.nuevoStock()))
                .thenReturn(Mono.just(producto));
        when(dtoMapper.toDto(producto)).thenReturn(responseDto);

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
    void testModificarStockProducto_BusinessException() {

        Long productoId = 201L;
        ProductoUpdateStockDTO requestDto = new ProductoUpdateStockDTO(-50);

        when(servicePort.modificarStockProducto(productoId, -50))
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
    void testModificarStockProducto_TechnicalException() {
        Long productoId = 201L;
        ProductoUpdateStockDTO requestDto = new ProductoUpdateStockDTO(100);
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.modificarStockProducto(productoId, requestDto.nuevoStock()))
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