package proyecto.nequi.api_franquicias.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Franquicias")
                        .description("API para gestión de franquicias, sucursales y productos")
                        .version("1.0"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Entorno de desarrollo"));
    }
}