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
import proyecto.nequi.api_franquicias.domain.api.FranchiseServicePort;
import proyecto.nequi.api_franquicias.domain.api.OptionalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.BranchWithProductos;
import proyecto.nequi.api_franquicias.domain.model.Franchise;
import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalWithProductsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranchiseDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.FranchiseWithDetailsDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.router.FranchiseRouter;
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
class FranchiseHandlerTest {

    @Mock
    private FranchiseServicePort franquiciaServicePort;

    @Mock
    private OptionalServicePort optionalServicePort;

    @Mock
    private FranchiseDTOMapper dtoMapper;

    @Mock
    private FranchiseWithDetailsDTOMapper detailsMapper;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {
        FranchiseRouter franchiseRouter = new FranchiseRouter();
        routerFunction = franchiseRouter.franchiseRoutes(franchiseHandler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void registerFranchise_Success() {
        FranchiseDTO requestDto = new FranchiseDTO(null, "Test Franchise");
        Franchise domain = new Franchise(null, "Test Franchise");
        Franchise savedDomain = new Franchise(1L, "Test Franchise");
        FranchiseDTO responseDto = new FranchiseDTO(1L, "Test Franchise");

        when(dtoMapper.toModel(any(FranchiseDTO.class))).thenReturn(domain);
        when(franquiciaServicePort.registerFranchise(any(Franchise.class))).thenReturn(Mono.just(savedDomain));
        when(dtoMapper.toDto(any(Franchise.class))).thenReturn(responseDto);

        webTestClient.post()
                .uri("/franchises")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_CREATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_CREATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo("Test Franchise");
    }

    @Test
    void registerFranchise_BusinessError() {
        FranchiseDTO requestDto = new FranchiseDTO(null, "Test Franchise");
        Franchise domain = new Franchise(null, "Test Franchise");

        when(dtoMapper.toModel(any(FranchiseDTO.class))).thenReturn(domain);
        when(franquiciaServicePort.registerFranchise(any(Franchise.class)))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NAME_FOUND)));

        webTestClient.post()
                .uri("/franchises")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NAME_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NAME_FOUND.getMessage());
    }

    @Test
    void registerFranchise_TechnicalError() {
        FranchiseDTO requestDto = new FranchiseDTO(null, "Test Franchise");
        Franchise domain = new Franchise(null, "Test Franchise");

        when(dtoMapper.toModel(any(FranchiseDTO.class))).thenReturn(domain);
        when(franquiciaServicePort.registerFranchise(any(Franchise.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalMessage.FAILED_TO_SAVE_ENTITY)));

        webTestClient.post()
                .uri("/franchises")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FAILED_TO_SAVE_ENTITY.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FAILED_TO_SAVE_ENTITY.getMessage());
    }

    @Test
    void updateFranchiseName_Success() {
        FranchiseUpdateDTO requestDto = new FranchiseUpdateDTO("Nuevo Nombre");
        Franchise updatedDomain = new Franchise(1L, "Nuevo Nombre");
        FranchiseDTO responseDto = new FranchiseDTO(1L, "Nuevo Nombre");

        when(franquiciaServicePort.updateFranchiseName(anyLong(), anyString())).thenReturn(Mono.just(updatedDomain));
        when(dtoMapper.toDto(any(Franchise.class))).thenReturn(responseDto);

        webTestClient.put()
                .uri("/franchises/1")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_UPDATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo("Nuevo Nombre");
    }

    @Test
    void updateFranchiseName_NotFound() {
        // Arrange
        FranchiseUpdateDTO requestDto = new FranchiseUpdateDTO("Nuevo Nombre");

        when(franquiciaServicePort.updateFranchiseName(anyLong(), anyString()))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));

        webTestClient.put()
                .uri("/franchises/999")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage());
    }

    @Test
    void updateFranchiseName_TechnicalError() {
        // Arrange
        FranchiseUpdateDTO requestDto = new FranchiseUpdateDTO("Nuevo Nombre");
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(franquiciaServicePort.updateFranchiseName(anyLong(), anyString()))
                .thenReturn(Mono.error(technicalException));

        // Act & Assert
        webTestClient.put()
                .uri("/franchises/1")
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void getFranchiseWithDetails_Success() {

        Long franquiciaId = 1L;
        List<BranchWithProductos> sucursales = Collections.emptyList();
        FranchiseWithDetails domain = new FranchiseWithDetails(franquiciaId, "Test Franchise", sucursales);

        List<SucursalWithProductsDTO> sucursalesDto = Collections.emptyList();
        FranchiseWithDetailsDTO responseDto = new FranchiseWithDetailsDTO(franquiciaId, "Test Franchise", sucursalesDto);

        when(optionalServicePort.getFranchiseWithDetails(franquiciaId)).thenReturn(Mono.just(domain));
        when(detailsMapper.toDto(domain)).thenReturn(responseDto);

        webTestClient.get()
                .uri("/franchises/1/full")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_FOUND.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo("Test Franchise");
    }

    @Test
    void getFranchiseWithDetails_NotFound() {
        Long franquiciaId = 999L;

        when(optionalServicePort.getFranchiseWithDetails(franquiciaId))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));

        webTestClient.get()
                .uri("/franchises/999/full")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage());
    }

    @Test
    void getFranchiseWithDetails_TechnicalException() {
        Long franquiciaId = 1L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(optionalServicePort.getFranchiseWithDetails(franquiciaId)).thenReturn(Mono.error(technicalException));

        webTestClient.get().uri("/franchises/{id}/full", franquiciaId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void handlerMethodsDirectInvocation() {
        ServerRequest registerRequest = mock(ServerRequest.class);
        FranchiseDTO requestDto = new FranchiseDTO(null, "Test Franchise");
        Franchise domain = new Franchise(null, "Test Franchise");
        Franchise savedDomain = new Franchise(1L, "Test Franchise");
        FranchiseDTO responseDto = new FranchiseDTO(1L, "Test Franchise");

        when(registerRequest.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(requestDto));
        when(dtoMapper.toModel(requestDto)).thenReturn(domain);
        when(franquiciaServicePort.registerFranchise(domain)).thenReturn(Mono.just(savedDomain));
        when(dtoMapper.toDto(savedDomain)).thenReturn(responseDto);

        StepVerifier.create(franchiseHandler.registerFranchise(registerRequest))
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
        ServerRequest updateRequest = mock(ServerRequest.class);
        when(updateRequest.pathVariable("id")).thenReturn("1");
        FranchiseUpdateDTO updateDto = new FranchiseUpdateDTO("Nuevo Nombre");

        when(updateRequest.bodyToMono(FranchiseUpdateDTO.class)).thenReturn(Mono.just(updateDto));
        when(franquiciaServicePort.updateFranchiseName(1L, "Nuevo Nombre"))
                .thenReturn(Mono.just(new Franchise(1L, "Nuevo Nombre")));

        StepVerifier.create(franchiseHandler.updateFranchiseName(updateRequest))
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
        ServerRequest getDetailsRequest = mock(ServerRequest.class);
        when(getDetailsRequest.pathVariable("id")).thenReturn("1");

        List<BranchWithProductos> sucursales = Collections.emptyList();
        FranchiseWithDetails detailsDomain = new FranchiseWithDetails(1L, "Test Franchise", sucursales);

        List<SucursalWithProductsDTO> sucursalesDto = Collections.emptyList();
        FranchiseWithDetailsDTO detailsDto = new FranchiseWithDetailsDTO(1L, "Test Franchise", sucursalesDto);

        when(optionalServicePort.getFranchiseWithDetails(1L)).thenReturn(Mono.just(detailsDomain));
        when(detailsMapper.toDto(detailsDomain)).thenReturn(detailsDto);
        StepVerifier.create(franchiseHandler.getFranchiseWithDetails(getDetailsRequest))
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void deleteFranchise_Success() {
        Long franquiciaId = 1L;
        when(franquiciaServicePort.deleteFranchise(franquiciaId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/franchises/{id}", franquiciaId)
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void deleteFranchise_NotFound() {
        Long franquiciaId = 999L;

        when(franquiciaServicePort.deleteFranchise(franquiciaId))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)));

        webTestClient.delete()
                .uri("/franchises/{id}", franquiciaId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage());
    }

    @Test
    void deleteFranchise_TechnicalError() {
        Long franquiciaId = 1L;

        when(franquiciaServicePort.deleteFranchise(franquiciaId))
                .thenReturn(Mono.error(new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR)));

        webTestClient.delete()
                .uri("/franchises/{id}", franquiciaId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }
}