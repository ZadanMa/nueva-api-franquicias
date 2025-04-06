package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;

import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.SucursalHandler;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SucursalRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    operation = @Operation(
                            summary = "Registrar sucursal",
                            tags = {"Sucursal"},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Sucursal creada", content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos o sucursal duplicada")
                            }
                    ),
                    path = "/sucursales"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Agregar sucursal a franquicia",
                            tags = {"Sucursal"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "franquiciaId", description = "ID de la franquicia"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Sucursal agregada", content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
                            }
                    ),
                    path = "/franquicias/{franquiciaId}/sucursales"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Obtener todas las sucursales",
                            tags = {"Sucursal"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de sucursales", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SucursalDTO.class))))
                            }
                    ),
                    path = "/sucursales"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Obtener sucursal por ID",
                            tags = {"Sucursal"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "sucursalId", description = "ID de la sucursal"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Sucursal encontrada", content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
                            }
                    ),
                    path = "/sucursales/{sucursalId}"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Actualizar nombre de sucursal",
                            tags = {"Sucursal"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "sucursalId", description = "ID de la sucursal"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SucursalUpdateDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Sucursal actualizada", content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
                            }
                    ),
                    path = "/sucursales/{sucursalId}/nombre"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Obtener productos con más stock por franquicia",
                            tags = {"Sucursal"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "franquiciaId", description = "ID de la franquicia"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Productos encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Map.class)))),
                                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
                            }
                    ),
                    path = "/franquicias/{franquiciaId}/productos-mas-stock"
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