package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.SucursalHandler;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SucursalRouter {

    @Bean
    public RouterFunction<ServerResponse> sucursalRoutes(SucursalHandler handler) {
        return route()
                .POST("/sucursales", handler::registrarSucursal)
                .POST("/franquicias/{franquiciaId}/sucursales", handler::agregarSucursal)
                .PUT("/sucursales/{sucursalId}/asociar-franquicia/{franquiciaId}", handler::asociarSucursalAFranquicia)
                .GET("/sucursales", handler::getAllSucursales)
                .GET("/sucursales/{sucursalId}", handler::getSucursalById)
                .PUT("/sucursales/{sucursalId}/nombre", handler::actualizarNombreSucursal)
                .GET("/franquicias/{franquiciaId}/productos-mas-stock", handler::productoConMasStockPorSucursal)
                .build();
    }
}