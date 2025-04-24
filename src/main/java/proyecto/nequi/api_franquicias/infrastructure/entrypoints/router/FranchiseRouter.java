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
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseWithDetailsDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.FranchiseHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/franquicias",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "registerFranquicia",
                    operation = @Operation(
                            summary = "Registrar franquicia",
                            tags = {"Franchise"},
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = FranchiseDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Franchise creada",
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
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateFranquiciaName",
                    operation = @Operation(
                            summary = "Actualizar name de franquicia",
                            tags = {"Franchise"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = FranchiseUpdateDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franchise actualizada", content = @Content(schema = @Schema(implementation = FranchiseDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franquicias/{id}/full",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "getFranquiciaWithDetails",
                    operation = @Operation(
                            summary = "Obtener franquicia con detalles",
                            tags = {"Franchise"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franchise encontrada", content = @Content(schema = @Schema(implementation = FranchiseWithDetailsDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franquicias/{id}",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.DELETE,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "deleteFranquicia",
                    operation = @Operation(
                            summary = "Eliminar franquicia",
                            tags = {"Franchise"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franchise eliminada", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise no encontrada")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return route()
                .POST("/franchises", handler::registerFranchise)
                .PUT("/franchises/{id}", handler::updateFranchiseName)
                .GET("/franchises/{id}/full", handler::getFranchiseWithDetails)
                .DELETE("/franchises/{id}", handler::deleteFranchise)
                .build();
    }
}