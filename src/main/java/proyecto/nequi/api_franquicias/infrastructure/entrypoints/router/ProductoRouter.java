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
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.ProductoHandler;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductoRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    operation = @Operation(
                            summary = "Registrar producto",
                            tags = {"Producto"},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos o producto duplicado")
                            }
                    ),
                    path = "/productos"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Agregar producto a sucursal",
                            tags = {"Producto"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "sucursalId", description = "ID de la sucursal"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Producto agregado", content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
                            }
                    ),
                    path = "/sucursales/{sucursalId}/productos"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Obtener todos los productos",
                            tags = {"Producto"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de productos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoDTO.class))))
                            }
                    ),
                    path = "/productos"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Obtener producto por ID",
                            tags = {"Producto"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "productoId", description = "ID del producto"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
                            }
                    ),
                    path = "/productos/{productoId}"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Eliminar producto",
                            tags = {"Producto"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "productoId", description = "ID del producto"),
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Producto eliminado"),
                                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
                            }
                    ),
                    path = "/productos/{productoId}"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Actualizar nombre de producto",
                            tags = {"Producto"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "productoId", description = "ID del producto"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductoUpdateDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Producto actualizado", content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
                            }
                    ),
                    path = "/productos/{productoId}/nombre"
            ),
            @RouterOperation(
                    operation = @Operation(
                            summary = "Modificar stock de producto",
                            tags = {"Producto"},
                            parameters = @Parameter(in = ParameterIn.PATH, name = "productoId", description = "ID del producto"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductoUpdateStockDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Stock actualizado", content = @Content(schema = @Schema(implementation = ProductoDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Stock negativo"),
                                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
                            }
                    ),
                    path = "/productos/{productoId}/stock"
            )
    })
    public RouterFunction<ServerResponse> productoRoutes(ProductoHandler handler) {
        return route()
                .POST("/productos", handler::registrarProducto)
                .GET("/productos", handler::getAllProductos)
                .GET("/productos/{productoId}", handler::getProductoById)
                .DELETE("/productos/{productoId}", handler::eliminarProducto)
                .PUT("/productos/{productoId}/nombre", handler::actualizarNombreProducto)
                .PUT("/productos/{productoId}/stock", handler::modificarStockProducto)
                .build();
    }
}