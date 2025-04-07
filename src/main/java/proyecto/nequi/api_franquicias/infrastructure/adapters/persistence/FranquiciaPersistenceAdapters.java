package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.domain.spi.FranquicePersistencePorts;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.*;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.ProductoRepository;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.SucursalRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaPersistenceAdapters implements FranquicePersistencePorts {
    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public FranquiciaPersistenceAdapters(FranquiciaRepository franquiciaRepository,
                                         SucursalRepository sucursalRepository,
                                         ProductoRepository productoRepository,
                                         ProductoMapper productoMapper) {
        this.franquiciaRepository = franquiciaRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public Mono<FranquiciaWithDetails> findWithDetailsById(Long id) {
        return franquiciaRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(franquiciaEntity -> {
                    Flux<SucursalWithProductos> sucursalesWithProducts = sucursalRepository.findByFranquiciaId(id)
                            .flatMap(sucursalEntity -> {
                                return productoRepository.findBySucursalId(sucursalEntity.getId())
                                        .map(productoMapper::toModel)
                                        .collectList()
                                        .map(productos -> new SucursalWithProductos(
                                                sucursalEntity.getId(),
                                                sucursalEntity.getNombre(),
                                                sucursalEntity.getFranquiciaId(),
                                                productos
                                        ));
                            });

                    return sucursalesWithProducts.collectList()
                            .map(sucursales -> new FranquiciaWithDetails(
                                    franquiciaEntity.getId(),
                                    franquiciaEntity.getNombre(),
                                    sucursales
                            ));
                });
    }

}