package proyecto.nequi.api_franquicias.domain.usecase;

import proyecto.nequi.api_franquicias.domain.api.SucursalServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.domain.spi.SucursalPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class SucursalUseCase implements SucursalServicePort {

    private final SucursalPersistencePort persistencePort;

    public SucursalUseCase(SucursalPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Sucursal> registrarSucursal(Sucursal sucursal) {
        return persistencePort.existsByFranquiciaIdAndNombre(sucursal.franquiciaId(), sucursal.nombre())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_ALREADY_EXISTS)))
                .flatMap(exists -> persistencePort.save(sucursal));
    }

    @Override
    public Flux<Sucursal> getAllSucursales() {
        return persistencePort.findAll();
    }

    @Override
    public Mono<Sucursal> getSucursalById(Long sucursalId) {
        return persistencePort.findById(sucursalId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)));
    }

    @Override
    public Mono<Sucursal> actualizarNombreSucursal(Long sucursalId, String nuevoNombre) {
        return persistencePort.findById(sucursalId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)))
                .flatMap(sucursal -> persistencePort.updateNombre(sucursalId, nuevoNombre));
    }

    @Override
    public Flux<Map<String, Object>> productoConMasStockPorSucursal(Long franquiciaId) {
        return persistencePort.productoConMasStockPorSucursal(franquiciaId);
    }
}