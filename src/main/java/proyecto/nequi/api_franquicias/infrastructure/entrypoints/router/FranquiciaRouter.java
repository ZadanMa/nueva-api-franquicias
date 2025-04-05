package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.FranquiciaHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranquiciaRouter {

    @Bean
    public RouterFunction<ServerResponse> franquiciaRoutes(FranquiciaHandler handler) {
        return route()
                .POST("/franquicias", handler::registerFranquicia)
                .PUT("/franquicias/{id}", handler::updateFranquicia)
                .GET("/franquicias/{id}/full", handler::getFranquiciaWithDetails)
                .build();
    }
}