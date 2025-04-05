package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.HealthHandler;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class HealthRouter {

    @Bean
    public RouterFunction<ServerResponse> healthRoutes(HealthHandler handler) {
        return route()
                .GET("/health", handler::health)
                .build();
    }
}