package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Tag(name = "Punto de salud", description = "Gestión de salud del sistema")
public class HealthHandler {

    public Mono<ServerResponse> health(ServerRequest request) {
        return ServerResponse.ok()
                .bodyValue(Map.of("status", "UP"));
    }
}