package proyecto.nequi.api_franquicias.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proyecto.nequi.api_franquicias.domain.api.FranquiciaServicePort;
import proyecto.nequi.api_franquicias.domain.api.SucursalServicePort;
import proyecto.nequi.api_franquicias.domain.api.ProductoServicePort;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import proyecto.nequi.api_franquicias.domain.spi.SucursalPersistencePort;
import proyecto.nequi.api_franquicias.domain.spi.ProductoPersistencePort;
import proyecto.nequi.api_franquicias.domain.usecase.FranquiciaUseCase;
import proyecto.nequi.api_franquicias.domain.usecase.SucursalUseCase;
import proyecto.nequi.api_franquicias.domain.usecase.ProductoUseCase;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    @Bean
    public FranquiciaServicePort franquiciaServicePort(FranquiciaPersistencePort franquiciaPersistencePort) {
        return new FranquiciaUseCase(franquiciaPersistencePort);
    }

    @Bean
    public SucursalServicePort sucursalServicePort(SucursalPersistencePort sucursalPersistencePort) {
        return new SucursalUseCase(sucursalPersistencePort);
    }

    @Bean
    public ProductoServicePort productoServicePort(ProductoPersistencePort productoPersistencePort) {
        return new ProductoUseCase(productoPersistencePort);
    }
}