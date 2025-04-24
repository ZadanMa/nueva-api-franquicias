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
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductUpdateStockDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.ProductHandler;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.util.APIResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/products",
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "registrarProducto",
                    operation = @Operation(
                            summary = "Registrar producto",
                            tags = {"Product"},
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProductDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Product creado",
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
                    path = "/products",
                    method = RequestMethod.GET,
                    beanClass = ProductHandler.class,
                    beanMethod = "getAllProductos",
                    operation = @Operation(
                            summary = "Obtener todos los products",
                            tags = {"Product"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de products",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/products/{productoId}",
                    method = RequestMethod.GET,
                    beanClass = ProductHandler.class,
                    beanMethod = "getProductoById",
                    operation = @Operation(
                            summary = "Obtener producto por ID",
                            tags = {"Product"},
                            parameters = {
                                    @Parameter(name = "productoId", in = ParameterIn.PATH, required = true, description = "ID del producto")
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product encontrado",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProductDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Product no encontrado",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = APIResponse.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/products/{productoId}",
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "eliminarProducto",
                    operation = @Operation(
                            summary = "Eliminar producto por ID",
                            tags = {"Product"},
                            parameters = {
                                    @Parameter(name="productoId", in=ParameterIn.PATH, required=true, description="ID del producto")
                            },
                            responses={
                                    @ApiResponse(
                                            responseCode="204",
                                            description="Product eliminado"
                                    ),
                                    @ApiResponse(
                                            responseCode="404",
                                            description="Product no encontrado"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path="/products/{productoId}/name",
                    method=RequestMethod.PUT,
                    beanClass= ProductHandler.class,
                    beanMethod="actualizarNombreProducto",
                    operation=@Operation(
                            summary="Actualizar name de producto",
                            tags={"Product"},
                            parameters={
                                    @Parameter(name="productoId", in=ParameterIn.PATH, required=true, description="ID del producto")
                            },
                            requestBody=@RequestBody(
                                    content=@Content(
                                            mediaType=MediaType.APPLICATION_JSON_VALUE,
                                            schema=@Schema(implementation= ProductUpdateDTO.class)
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
                                            description="Product no encontrado"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path="/products/{productoId}/stock",
                    method=RequestMethod.PUT,
                    beanClass= ProductHandler.class,
                    beanMethod="modificarStockProducto",
                    operation=@Operation(
                            summary="Modificar stock de producto",
                            tags={"Product"},
                            parameters={
                                    @Parameter(name="productoId", in=ParameterIn.PATH, required=true, description="ID del producto")
                            },
                            requestBody=@RequestBody(
                                    content=@Content(
                                            mediaType=MediaType.APPLICATION_JSON_VALUE,
                                            schema=@Schema(implementation= ProductUpdateStockDTO.class)
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
                                            description="Product no encontrado"
                                    )
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> productoRoutes(ProductHandler handler) {
        return route()
                .POST("/productos", handler::registerProduct)
                .GET("/productos", handler::getAllProducts)
                .GET("/productos/{productoId}", handler::getProductById)
                .DELETE("/productos/{productoId}", handler::deleteProduct)
                .PUT("/productos/{productoId}/nombre", handler::updateNameProduct)
                .PUT("/productos/{productoId}/stock", handler::modifyStockProduct)
                .build();
    }
}