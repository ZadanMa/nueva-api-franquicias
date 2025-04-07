package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.FranquiciaHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranquiciaRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/franquicias",
                    method = RequestMethod.POST,
                    beanClass = FranquiciaHandler.class,
                    beanMethod = "registerFranquicia",
                    operation = @Operation(
                            summary = "Registrar franquicia",
                            tags = {"Franquicia"},
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = FranquiciaDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Franquicia creada",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = APIResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de negocio",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = APIResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franquicias/{id}",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.PUT,
                    beanClass = FranquiciaHandler.class,
                    beanMethod = "updateFranquiciaName",
                    operation = @Operation(
                            summary = "Actualizar nombre de franquicia",
                            tags = {"Franquicia"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = FranquiciaUpdateDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franquicia actualizada", content = @Content(schema = @Schema(implementation = FranquiciaDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franquicias/{id}/full",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.GET,
                    beanClass = FranquiciaHandler.class,
                    beanMethod = "getFranquiciaWithDetails",
                    operation = @Operation(
                            summary = "Obtener franquicia con detalles",
                            tags = {"Franquicia"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franquicia encontrada", content = @Content(schema = @Schema(implementation = FranquiciaWithDetailsDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> franquiciaRoutes(FranquiciaHandler handler) {
        return route()
                .POST("/franquicias", handler::registerFranquicia)
                .PUT("/franquicias/{id}", handler::updateFranquiciaName)
                .GET("/franquicias/{id}/full", handler::getFranquiciaWithDetails)
                .build();
    }
}