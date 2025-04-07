package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.SucursalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.SucursalDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.router.SucursalRouter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalHandlerTest {

    @Mock
    private SucursalServicePort servicePort;
    @Mock
    private SucursalDTOMapper dtoMapper;
    @InjectMocks
    private SucursalHandler handler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {
        SucursalRouter sucursalRouter = new SucursalRouter();
        routerFunction = sucursalRouter.sucursalRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void testRegistrarSucursal_Success() {
        SucursalDTO requestDto = new SucursalDTO(null, "Sucursal A", 1L);
        Sucursal sucursal = new Sucursal(101L, "Sucursal A", 1L);
        SucursalDTO responseDto = new SucursalDTO(101L, "Sucursal A", 1L);

        when(dtoMapper.toModel(requestDto)).thenReturn(sucursal);
        when(servicePort.registrarSucursal(sucursal)).thenReturn(Mono.just(sucursal));
        when(dtoMapper.toDto(sucursal)).thenReturn(responseDto);

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_CREATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_CREATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(101)
                .jsonPath("$.data.nombre").isEqualTo("Sucursal A")
                .jsonPath("$.data.franquiciaId").isEqualTo(1);
    }

    @Test
    void testRegistrarSucursal_BusinessException() {

        SucursalDTO requestDto = new SucursalDTO(null, "Sucursal A", 1L);
        Sucursal sucursal = new Sucursal(101L, "Sucursal A", 1L);

        when(dtoMapper.toModel(requestDto)).thenReturn(sucursal);
        when(servicePort.registrarSucursal(sucursal))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_ALREADY_EXISTS)));

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_ALREADY_EXISTS.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_ALREADY_EXISTS.getMessage());
    }
    @Test
    void testRegistrarSucursal_TechnicalException() {
        SucursalDTO requestDto = new SucursalDTO(null, "Sucursal A", 1L);
        Sucursal sucursal = new Sucursal(101L, "Sucursal A", 1L);
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(dtoMapper.toModel(requestDto)).thenReturn(sucursal);
        when(servicePort.registrarSucursal(sucursal)).thenReturn(Mono.error(technicalException));

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetAllSucursales_Success() {
        Sucursal sucursal1 = new Sucursal(101L, "Sucursal A", 1L);
        Sucursal sucursal2 = new Sucursal(102L, "Sucursal B", 1L);
        List<Sucursal> sucursales = List.of(sucursal1, sucursal2);

        when(servicePort.getAllSucursales()).thenReturn(Flux.fromIterable(sucursales));
        when(dtoMapper.toDto(any(Sucursal.class))).thenReturn(new SucursalDTO(101L, "Sucursal A", 1L));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getMessage())
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[0].id").isEqualTo(101)
                .jsonPath("$.data[0].nombre").isEqualTo("Sucursal A");
    }
    @Test
    void testGetAllSucursales_BusinessException() {
        when(servicePort.getAllSucursales()).thenReturn(Flux.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getMessage());
    }

    @Test
    void testGetAllSucursales_TechnicalException() {
        when(servicePort.getAllSucursales()).thenReturn(Flux.error(new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR)));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetSucursalById_Success() {
        Long sucursalId = 101L;
        Sucursal sucursal = new Sucursal(sucursalId, "Sucursal A", 1L);
        SucursalDTO responseDto = new SucursalDTO(sucursalId, "Sucursal A", 1L);

        when(servicePort.getSucursalById(sucursalId)).thenReturn(Mono.just(sucursal));
        when(dtoMapper.toDto(sucursal)).thenReturn(responseDto);

        webTestClient.get().uri("/sucursales/{sucursalId}", sucursalId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getMessage())
                .jsonPath("$.data.id").isEqualTo(101)
                .jsonPath("$.data.nombre").isEqualTo("Sucursal A");
    }


    @Test
    void testGetSucursalById_NotFound() {
        // Arrange
        Long sucursalId = 101L;

        when(servicePort.getSucursalById(sucursalId)).thenReturn(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)));

        // Act & Assert
        webTestClient.get().uri("/sucursales/{sucursalId}", sucursalId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getMessage());
    }

    @Test
    void testGetSucursalById_TechnicalException() {
        Long sucursalId = 101L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.getSucursalById(sucursalId)).thenReturn(Mono.error(technicalException));

        webTestClient.get().uri("/sucursales/{sucursalId}", sucursalId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testActualizarNombreSucursal_Success() {

        Long sucursalId = 101L;
        SucursalUpdateDTO requestDto = new SucursalUpdateDTO("Sucursal Nueva");
        Sucursal sucursalActualizada = new Sucursal(sucursalId, "Sucursal Nueva", 1L);
        SucursalDTO responseDto = new SucursalDTO(sucursalId, "Sucursal Nueva", 1L);

        when(servicePort.actualizarNombreSucursal(sucursalId, requestDto.nuevoNombre()))
                .thenReturn(Mono.just(sucursalActualizada));
        when(dtoMapper.toDto(sucursalActualizada)).thenReturn(responseDto);


        webTestClient.put().uri("/sucursales/{sucursalId}/nombre", sucursalId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_UPDATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(101)
                .jsonPath("$.data.nombre").isEqualTo("Sucursal Nueva");
    }

    @Test
    void testActualizarNombreSucursal_BusinessException() {
        Long sucursalId = 101L;
        SucursalUpdateDTO requestDto = new SucursalUpdateDTO("Sucursal Nueva");
        BusinessException businessException = new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND);

        when(servicePort.actualizarNombreSucursal(sucursalId, requestDto.nuevoNombre()))
                .thenReturn(Mono.error(businessException));

        webTestClient.put().uri("/sucursales/{sucursalId}/nombre", sucursalId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getMessage());
    }

    @Test
    void testActualizarNombreSucursal_TechnicalException() {
        Long sucursalId = 101L;
        SucursalUpdateDTO requestDto = new SucursalUpdateDTO("Sucursal Nueva");
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.actualizarNombreSucursal(sucursalId, requestDto.nuevoNombre()))
                .thenReturn(Mono.error(technicalException));

        webTestClient.put().uri("/sucursales/{sucursalId}/nombre", sucursalId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testProductoConMasStockPorSucursal_Success() {

        Long franquiciaId = 1L;
        Map<String, Object> productoData = Map.of(
                "producto_nombre", "Hamburguesa",
                "stock", 100,
                "sucursal_id", 101L
        );

        when(servicePort.productoConMasStockPorSucursal(franquiciaId))
                .thenReturn(Flux.just(productoData));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos-mas-stock", franquiciaId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                .jsonPath("$.data[0].producto_nombre").isEqualTo("Hamburguesa")
                .jsonPath("$.data[0].stock").isEqualTo(100)
                .jsonPath("$.data[0].sucursal_id").isEqualTo(101);
    }

    @Test
    void testProductoConMasStockPorSucursal_BusinessException() {
        Long franquiciaId = 1L;
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.productoConMasStockPorSucursal(franquiciaId)).thenReturn(Flux.error(businessException));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos-mas-stock", franquiciaId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testProductoConMasStockPorSucursal_TechnicalException() {
        Long franquiciaId = 1L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.productoConMasStockPorSucursal(franquiciaId)).thenReturn(Flux.error(technicalException));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos-mas-stock", franquiciaId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

}