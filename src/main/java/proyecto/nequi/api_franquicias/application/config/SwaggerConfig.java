package proyecto.nequi.api_franquicias.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Franquicias Nequi")
                        .version("1.0")
                        .description("API para gestión de franquicias, sucursales y productos"));
    }
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api-franquicias")
                .pathsToMatch("/franquicias/**", "/sucursales/**", "/productos/**")
                .build();
    }
}