package proyecto.nequi.api_franquicias.infrastructure.entrypoints.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler.HealthHandler;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class HealthRouter {

    @Bean
    @RouterOperation(
            path = "/health",
            method = RequestMethod.GET,
            beanClass = HealthHandler.class,
            beanMethod = "health",
            operation = @Operation(
                    summary = "Punto de salud",
                    tags = {"Salud"},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Sistema en funcionamiento", content = @Content(schema = @Schema(implementation = Map.class))),
                            @ApiResponse(responseCode = "500", description = "Sistema fuera de servicio")
                    })

    )
    public RouterFunction<ServerResponse> healthRoutes(HealthHandler handler) {
        return route()
                .GET("/health", handler::health)
                .build();
    }
}