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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.ProductoHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductoRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/productos",
                    method = RequestMethod.POST,
                    beanClass = ProductoHandler.class,
                    beanMethod = "registrarProducto",
                    operation = @Operation(
                            summary = "Registrar producto",
                            tags = {"Producto"},
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProductoDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Producto creado",
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
                    path = "/productos",
                    method = RequestMethod.GET,
                    beanClass = ProductoHandler.class,
                    beanMethod = "getAllProductos",
                    operation = @Operation(
                            summary = "Obtener todos los productos",
                            tags = {"Producto"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de productos",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = ProductoDTO.class))
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/productos/{productoId}",
                    method = RequestMethod.GET,
                    beanClass = ProductoHandler.class,
                    beanMethod = "getProductoById",
                    operation = @Operation(
                            summary = "Obtener producto por ID",
                            tags = {"Producto"},
                            parameters = {
                                    @Parameter(name = "productoId", in = ParameterIn.PATH, required = true, description = "ID del producto")
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Producto encontrado",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProductoDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Producto no encontrado",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = APIResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/productos/{productoId}",
                    method = RequestMethod.DELETE,
                    beanClass = ProductoHandler.class,
                    beanMethod = "eliminarProducto",
                    operation = @Operation(
                            summary = "Eliminar producto por ID",
                            tags = {"Producto"},
                            parameters = {
                                    @Parameter(name="productoId", in=ParameterIn.PATH, required=true, description="ID del producto")
                            },
                            responses={
                                    @ApiResponse(
                                            responseCode="204",
                                            description="Producto eliminado"
                                    ),
                                    @ApiResponse(
                                            responseCode="404",
                                            description="Producto no encontrado"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path="/productos/{productoId}/nombre",
                    method=RequestMethod.PUT,
                    beanClass=ProductoHandler.class,
                    beanMethod="actualizarNombreProducto",
                    operation=@Operation(
                            summary="Actualizar nombre de producto",
                            tags={"Producto"},
                            parameters={
                                    @Parameter(name="productoId", in=ParameterIn.PATH, required=true, description="ID del producto")
                            },
                            requestBody=@RequestBody(
                                    content=@Content(
                                            mediaType=MediaType.APPLICATION_JSON_VALUE,
                                            schema=@Schema(implementation=ProductoUpdateDTO.class)
                                    )
                            ),
                            responses={
                                    @ApiResponse(
                                            responseCode="200",
                                            description="Nombre de producto actualizado",
                                            content=@Content(
                                                    mediaType=MediaType.APPLICATION_JSON_VALUE,
                                                    schema=@Schema(implementation=APIResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode="404",
                                            description="Producto no encontrado"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path="/productos/{productoId}/stock",
                    method=RequestMethod.PUT,
                    beanClass=ProductoHandler.class,
                    beanMethod="modificarStockProducto",
                    operation=@Operation(
                            summary="Modificar stock de producto",
                            tags={"Producto"},
                            parameters={
                                    @Parameter(name="productoId", in=ParameterIn.PATH, required=true, description="ID del producto")
                            },
                            requestBody=@RequestBody(
                                    content=@Content(
                                            mediaType=MediaType.APPLICATION_JSON_VALUE,
                                            schema=@Schema(implementation=ProductoUpdateStockDTO.class)
                                    )
                            ),
                            responses={
                                    @ApiResponse(
                                            responseCode="200",
                                            description="Stock de producto modificado",
                                            content=@Content(
                                                    mediaType=MediaType.APPLICATION_JSON_VALUE,
                                                    schema=@Schema(implementation=APIResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode="404",
                                            description="Producto no encontrado"
                                    )
                            }
                    )
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