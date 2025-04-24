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
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.BranchHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/sucursales",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = BranchHandler.class,
                    beanMethod = "registrarSucursal",
                    operation = @Operation(
                            summary = "Registrar sucursal",
                            tags = {"Sucursales"},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = BranchDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Branch creada", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Branch duplicada", content = @Content(schema = @Schema(implementation = APIResponse.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/sucursales/{branchId}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = BranchHandler.class,
                    beanMethod = "getSucursalById",
                    operation = @Operation(
                            summary = "Obtener sucursal por ID",
                            tags = {"Sucursales"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "branchId", description = "ID de la sucursal"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Branch encontrada", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Branch no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/sucursales/{branchId}/name",
                    method = RequestMethod.PUT,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = BranchHandler.class,
                    beanMethod = "actualizarNombreSucursal",
                    operation = @Operation(
                            summary = "Actualizar name de sucursal",
                            tags = {"Sucursales"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "branchId", description = "ID de la sucursal"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = BranchUpdateDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Nombre actualizado", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Branch no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franquicias/{franchiseId}/products-mas-stock",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = BranchHandler.class,
                    beanMethod = "productoConMasStockPorSucursal",
                    operation = @Operation(
                            summary = "Product con más stock por sucursal",
                            tags = {"Sucursales"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "ID de la franquicia"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Productos encontrados", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise no encontrada")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> sucursalRoutes(BranchHandler handler) {
        return route()
                .POST("/sucursales", handler::registerBranch)
                .GET("/sucursales", handler::getAllBranch)
                .GET("/sucursales/{sucursalId}", handler::getBranchById)
                .PUT("/sucursales/{sucursalId}/nombre", handler::updateNameBranch)
                .GET("/franchises/{franquiciaId}/productos-mas-stock", handler::productMostStockPerBranch)
                .build();
    }
}