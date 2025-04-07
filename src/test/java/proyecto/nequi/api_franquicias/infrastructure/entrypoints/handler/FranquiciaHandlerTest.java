package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.FranquiciaServicePort;
import proyecto.nequi.api_franquicias.domain.api.OptionalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalWithProductsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranquiciaDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranquiciaWithDetailsDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.router.FranquiciaRouter;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaHandlerTest {

    @Mock
    private FranquiciaServicePort franquiciaServicePort;

    @Mock
    private OptionalServicePort optionalServicePort;

    @Mock
    private FranquiciaDTOMapper dtoMapper;

    @Mock
    private FranquiciaWithDetailsDTOMapper detailsMapper;

    @InjectMocks
    private FranquiciaHandler franquiciaHandler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {
        FranquiciaRouter franquiciaRouter = new FranquiciaRouter();
        routerFunction = franquiciaRouter.franquiciaRoutes(franquiciaHandler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void registerFranquicia_Success() {
        FranquiciaDTO requestDto = new FranquiciaDTO(null, "Test Franquicia");
        Franquicia domain = new Franquicia(null, "Test Franquicia");
        Franquicia savedDomain = new Franquicia(1L, "Test Franquicia");
        FranquiciaDTO responseDto = new FranquiciaDTO(1L, "Test Franquicia");

        when(dtoMapper.toModel(any(FranquiciaDTO.class))).thenReturn(domain);
        when(franquiciaServicePort.registerFranquicia(any(Franquicia.class))).thenReturn(Mono.just(savedDomain));
        when(dtoMapper.toDto(any(Franquicia.class))).thenReturn(responseDto);

        webTestClient.post()
                .uri("/franquicias")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_CREATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_CREATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.nombre").isEqualTo("Test Franquicia");
    }

    @Test
    void registerFranquicia_BusinessError() {
        FranquiciaDTO requestDto = new FranquiciaDTO(null, "Test Franquicia");
        Franquicia domain = new Franquicia(null, "Test Franquicia");

        when(dtoMapper.toModel(any(FranquiciaDTO.class))).thenReturn(domain);
        when(franquiciaServicePort.registerFranquicia(any(Franquicia.class)))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NAME_FOUND)));

        webTestClient.post()
                .uri("/franquicias")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NAME_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NAME_FOUND.getMessage());
    }

    @Test
    void registerFranquicia_TechnicalError() {
        FranquiciaDTO requestDto = new FranquiciaDTO(null, "Test Franquicia");
        Franquicia domain = new Franquicia(null, "Test Franquicia");

        when(dtoMapper.toModel(any(FranquiciaDTO.class))).thenReturn(domain);
        when(franquiciaServicePort.registerFranquicia(any(Franquicia.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY)));

        webTestClient.post()
                .uri("/franquicias")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FAILED_TO_SAVE_ENTITY.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FAILED_TO_SAVE_ENTITY.getMessage());
    }

    @Test
    void updateFranquiciaName_Success() {
        FranquiciaUpdateDTO requestDto = new FranquiciaUpdateDTO("Nuevo Nombre");
        Franquicia updatedDomain = new Franquicia(1L, "Nuevo Nombre");
        FranquiciaDTO responseDto = new FranquiciaDTO(1L, "Nuevo Nombre");

        when(franquiciaServicePort.updateFranquiciaName(anyLong(), anyString())).thenReturn(Mono.just(updatedDomain));
        when(dtoMapper.toDto(any(Franquicia.class))).thenReturn(responseDto);

        webTestClient.put()
                .uri("/franquicias/1")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_UPDATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.nombre").isEqualTo("Nuevo Nombre");
    }

    @Test
    void updateFranquiciaName_NotFound() {
        // Arrange
        FranquiciaUpdateDTO requestDto = new FranquiciaUpdateDTO("Nuevo Nombre");

        when(franquiciaServicePort.updateFranquiciaName(anyLong(), anyString()))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));

        webTestClient.put()
                .uri("/franquicias/999")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage());
    }

    @Test
    void updateFranquiciaName_TechnicalError() {
        // Arrange
        FranquiciaUpdateDTO requestDto = new FranquiciaUpdateDTO("Nuevo Nombre");
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(franquiciaServicePort.updateFranquiciaName(anyLong(), anyString()))
                .thenReturn(Mono.error(technicalException));

        // Act & Assert
        webTestClient.put()
                .uri("/franquicias/1")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void getFranquiciaWithDetails_Success() {

        Long franquiciaId = 1L;
        List<SucursalWithProductos> sucursales = Collections.emptyList();
        FranquiciaWithDetails domain = new FranquiciaWithDetails(franquiciaId, "Test Franquicia", sucursales);

        List<SucursalWithProductsDTO> sucursalesDto = Collections.emptyList();
        FranquiciaWithDetailsDTO responseDto = new FranquiciaWithDetailsDTO(franquiciaId, "Test Franquicia", sucursalesDto);

        when(optionalServicePort.getFranquiciaWithDetails(franquiciaId)).thenReturn(Mono.just(domain));
        when(detailsMapper.toDto(domain)).thenReturn(responseDto);

        webTestClient.get()
                .uri("/franquicias/1/full")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_FOUND.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.nombre").isEqualTo("Test Franquicia");
    }

    @Test
    void getFranquiciaWithDetails_NotFound() {
        Long franquiciaId = 999L;

        when(optionalServicePort.getFranquiciaWithDetails(franquiciaId))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));

        webTestClient.get()
                .uri("/franquicias/999/full")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage());
    }

    @Test
    void getFranquiciaWithDetails_TechnicalException() {
        Long franquiciaId = 1L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(optionalServicePort.getFranquiciaWithDetails(franquiciaId)).thenReturn(Mono.error(technicalException));

        webTestClient.get().uri("/franquicias/{id}/full", franquiciaId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void handlerMethodsDirectInvocation() {
        ServerRequest registerRequest = mock(ServerRequest.class);
        FranquiciaDTO requestDto = new FranquiciaDTO(null, "Test Franquicia");
        Franquicia domain = new Franquicia(null, "Test Franquicia");
        Franquicia savedDomain = new Franquicia(1L, "Test Franquicia");
        FranquiciaDTO responseDto = new FranquiciaDTO(1L, "Test Franquicia");

        when(registerRequest.bodyToMono(FranquiciaDTO.class)).thenReturn(Mono.just(requestDto));
        when(dtoMapper.toModel(requestDto)).thenReturn(domain);
        when(franquiciaServicePort.registerFranquicia(domain)).thenReturn(Mono.just(savedDomain));
        when(dtoMapper.toDto(savedDomain)).thenReturn(responseDto);

        StepVerifier.create(franquiciaHandler.registerFranquicia(registerRequest))
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
        ServerRequest updateRequest = mock(ServerRequest.class);
        when(updateRequest.pathVariable("id")).thenReturn("1");
        FranquiciaUpdateDTO updateDto = new FranquiciaUpdateDTO("Nuevo Nombre");

        when(updateRequest.bodyToMono(FranquiciaUpdateDTO.class)).thenReturn(Mono.just(updateDto));
        when(franquiciaServicePort.updateFranquiciaName(1L, "Nuevo Nombre"))
                .thenReturn(Mono.just(new Franquicia(1L, "Nuevo Nombre")));

        StepVerifier.create(franquiciaHandler.updateFranquiciaName(updateRequest))
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
        ServerRequest getDetailsRequest = mock(ServerRequest.class);
        when(getDetailsRequest.pathVariable("id")).thenReturn("1");

        List<SucursalWithProductos> sucursales = Collections.emptyList();
        FranquiciaWithDetails detailsDomain = new FranquiciaWithDetails(1L, "Test Franquicia", sucursales);

        List<SucursalWithProductsDTO> sucursalesDto = Collections.emptyList();
        FranquiciaWithDetailsDTO detailsDto = new FranquiciaWithDetailsDTO(1L, "Test Franquicia", sucursalesDto);

        when(optionalServicePort.getFranquiciaWithDetails(1L)).thenReturn(Mono.just(detailsDomain));
        when(detailsMapper.toDto(detailsDomain)).thenReturn(detailsDto);
        StepVerifier.create(franquiciaHandler.getFranquiciaWithDetails(getDetailsRequest))
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }
}