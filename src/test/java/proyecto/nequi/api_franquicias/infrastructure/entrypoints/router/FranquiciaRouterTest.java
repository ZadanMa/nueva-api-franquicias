package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.FranquiciaHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaRouterTest {

    @Mock
    private FranquiciaHandler handler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {
        FranquiciaRouter router = new FranquiciaRouter();
        routerFunction = router.franquiciaRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testRegisterFranquiciaRoute() {
        // Arrange
        FranquiciaDTO dto = new FranquiciaDTO(null, "Test Franquicia");
        FranquiciaDTO responseDto = new FranquiciaDTO(1L, "Test Franquicia");

        APIResponse<FranquiciaDTO> apiResponse = APIResponse.<FranquiciaDTO>builder()
                .code(TechnicalMessage.FRANQUICIA_CREATED.getCode())
                .message(TechnicalMessage.FRANQUICIA_CREATED.getMessage())
                .data(responseDto)
                .build();

        when(handler.registerFranquicia(any())).thenReturn(
                ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(apiResponse)
        );

        // Act & Assert
        webTestClient.post()
                .uri("/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_CREATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_CREATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.nombre").isEqualTo("Test Franquicia");
    }

    @Test
    void testUpdateFranquiciaNameRoute() {
        // Arrange
        FranquiciaUpdateDTO updateDto = new FranquiciaUpdateDTO("Nuevo Nombre");
        FranquiciaDTO responseDto = new FranquiciaDTO(1L, "Nuevo Nombre");

        APIResponse<FranquiciaDTO> apiResponse = APIResponse.<FranquiciaDTO>builder()
                .code(TechnicalMessage.FRANQUICIA_UPDATED.getCode())
                .message(TechnicalMessage.FRANQUICIA_UPDATED.getMessage())
                .data(responseDto)
                .build();

        when(handler.updateFranquiciaName(any())).thenReturn(
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(apiResponse)
        );

        // Act & Assert
        webTestClient.put()
                .uri("/franquicias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_UPDATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.nombre").isEqualTo("Nuevo Nombre");
    }

    @Test
    void testGetFranquiciaWithDetailsRoute() {
        // Arrange
        FranquiciaWithDetailsDTO detailsDto = new FranquiciaWithDetailsDTO(1L, "Test Franquicia", Collections.emptyList());

        APIResponse<FranquiciaWithDetailsDTO> apiResponse = APIResponse.<FranquiciaWithDetailsDTO>builder()
                .code(TechnicalMessage.FRANQUICIA_FOUND.getCode())
                .message(TechnicalMessage.FRANQUICIA_FOUND.getMessage())
                .data(detailsDto)
                .build();

        when(handler.getFranquiciaWithDetails(any())).thenReturn(
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(apiResponse)
        );

        // Act & Assert
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
    void testRouteNotFound() {
        // Verificar que una ruta no definida retorna 404
        webTestClient.get()
                .uri("/franquicias/ruta-que-no-existe")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateFranquiciaNameRouteWithError() {
        // Arrange
        FranquiciaUpdateDTO updateDto = new FranquiciaUpdateDTO("Nuevo Nombre");

        APIResponse<?> errorResponse = APIResponse.builder()
                .code(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .message(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage())
                .build();

        when(handler.updateFranquiciaName(any())).thenReturn(
                ServerResponse.status(404)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(errorResponse)
        );

        // Act & Assert
        webTestClient.put()
                .uri("/franquicias/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.FRANQUICIA_NOT_FOUND.getMessage());
    }
}