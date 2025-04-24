package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.BranchWithProductos;
import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import proyecto.nequi.api_franquicias.domain.spi.FranchisePersistencePorts;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.*;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranchiseRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.BranchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FranchisePersistenceAdapters implements FranchisePersistencePorts {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public FranchisePersistenceAdapters(FranchiseRepository franchiseRepository,
                                        BranchRepository branchRepository,
                                        ProductRepository productRepository,
                                        ProductMapper productMapper) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<FranchiseWithDetails> findWithDetailsById(Long id) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(franquiciaEntity -> {
                    Flux<BranchWithProductos> sucursalesWithProducts = branchRepository.findByFranchiseId(id)
                            .flatMap(sucursalEntity -> {
                                return productRepository.findByBranchId(sucursalEntity.getId())
                                        .map(productMapper::toModel)
                                        .collectList()
                                        .map(productos -> new BranchWithProductos(
                                                sucursalEntity.getId(),
                                                sucursalEntity.getName(),
                                                sucursalEntity.getFranchiseId(),
                                                productos
                                        ));
                            });

                    return sucursalesWithProducts.collectList()
                            .map(sucursales -> new FranchiseWithDetails(
                                    franquiciaEntity.getId(),
                                    franquiciaEntity.getName(),
                                    sucursales
                            ));
                });
    }

}