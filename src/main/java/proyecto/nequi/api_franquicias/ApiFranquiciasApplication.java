package proyecto.nequi.api_franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "proyecto.nequi.api_franquicias.domain",
                "proyecto.nequi.api_franquicias.application",
                "proyecto.nequi.api_franquicias.infrastructure.entrypoints",
                "proyecto.nequi.api_franquicias.infrastructure.adapters",
                "proyecto.nequi.api_franquicias.application.config",
                "proyecto.nequi.api_franquicias.domain.usecase",
                "proyecto.nequi.api_franquicias.infrastructure.adapters.persistence",
        }
)
public class ApiFranquiciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiFranquiciasApplication.class, args);
    }

}
