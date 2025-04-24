package proyecto.nequi.api_franquicias.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proyecto.nequi.api_franquicias.domain.api.FranchiseServicePort;
import proyecto.nequi.api_franquicias.domain.api.OptionalServicePort;
import proyecto.nequi.api_franquicias.domain.api.BranchServicePort;
import proyecto.nequi.api_franquicias.domain.api.ProductServicePort;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePorts;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePort;
import proyecto.nequi.api_franquicias.domain.spi.BranchPersistencePort;
import proyecto.nequi.api_franquicias.domain.spi.ProductPersistencePort;
import proyecto.nequi.api_franquicias.domain.usecase.FranchiseUseCase;
import proyecto.nequi.api_franquicias.domain.usecase.OptionalUseCase;
import proyecto.nequi.api_franquicias.domain.usecase.BranchUseCase;
import proyecto.nequi.api_franquicias.domain.usecase.ProductUseCase;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    @Bean
    public FranchiseServicePort franchiseServicePort(FranchisePersistencePort franchisePersistencePort) {
        return new FranchiseUseCase(franchisePersistencePort);
    }

    @Bean
    public BranchServicePort branchServicePort(BranchPersistencePort branchPersistencePort) {
        return new BranchUseCase(branchPersistencePort);
    }

    @Bean
    public ProductServicePort productServicePort(ProductPersistencePort productPersistencePort) {
        return new ProductUseCase(productPersistencePort);
    }
    @Bean
    public OptionalServicePort optionalServicePort(FranchisePersistencePorts optionalServicePort) {
        return new OptionalUseCase(optionalServicePort);
    }
}