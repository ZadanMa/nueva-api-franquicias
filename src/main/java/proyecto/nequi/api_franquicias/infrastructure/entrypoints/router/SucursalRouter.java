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
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.SucursalHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SucursalRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/sucursales",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = SucursalHandler.class,
                    beanMethod = "registrarSucursal",
                    operation = @Operation(
                            summary = "Registrar sucursal",
                            tags = {"Sucursales"},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Sucursal creada", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Sucursal duplicada", content = @Content(schema = @Schema(implementation = APIResponse.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/sucursales/{sucursalId}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = SucursalHandler.class,
                    beanMethod = "getSucursalById",
                    operation = @Operation(
                            summary = "Obtener sucursal por ID",
                            tags = {"Sucursales"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "sucursalId", description = "ID de la sucursal"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Sucursal encontrada", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/sucursales/{sucursalId}/nombre",
                    method = RequestMethod.PUT,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = SucursalHandler.class,
                    beanMethod = "actualizarNombreSucursal",
                    operation = @Operation(
                            summary = "Actualizar nombre de sucursal",
                            tags = {"Sucursales"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "sucursalId", description = "ID de la sucursal"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SucursalUpdateDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Nombre actualizado", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franquicias/{franquiciaId}/productos-mas-stock",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = SucursalHandler.class,
                    beanMethod = "productoConMasStockPorSucursal",
                    operation = @Operation(
                            summary = "Producto con más stock por sucursal",
                            tags = {"Sucursales"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "franquiciaId", description = "ID de la franquicia"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Productos encontrados", content = @Content(schema = @Schema(implementation = APIResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> sucursalRoutes(SucursalHandler handler) {
        return route()
                .POST("/sucursales", handler::registrarSucursal)
                .GET("/sucursales", handler::getAllSucursales)
                .GET("/sucursales/{sucursalId}", handler::getSucursalById)
                .PUT("/sucursales/{sucursalId}/nombre", handler::actualizarNombreSucursal)
                .GET("/franquicias/{franquiciaId}/productos-mas-stock", handler::productoConMasStockPorSucursal)
                .build();
    }
}