package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.ProductoHandler;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductoRouter {

    @Bean
    public RouterFunction<ServerResponse> productoRoutes(ProductoHandler handler) {
        return route()
                .POST("/productos", handler::registrarProducto)
                .POST("/sucursales/{sucursalId}/productos", handler::agregarProducto)
                .PUT("/productos/{productoId}/asociar-sucursal/{sucursalId}", handler::asociarProductoASucursal)
                .GET("/productos", handler::getAllProductos)
                .GET("/productos/{productoId}", handler::getProductoById)
                .DELETE("/productos/{productoId}", handler::eliminarProducto)
                .PUT("/productos/{productoId}/nombre", handler::actualizarNombreProducto)
                .PUT("/productos/{productoId}/stock", handler::modificarStockProducto)
                .build();
    }
}