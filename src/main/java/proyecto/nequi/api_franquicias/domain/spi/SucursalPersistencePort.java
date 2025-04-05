package proyecto.nequi.api_franquicias.domain.spi;

import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface SucursalPersistencePort {
    Mono<Sucursal> save(Sucursal sucursal);
    Mono<Boolean> existsByFranquiciaIdAndNombre(Long franquiciaId, String nombre);
    Mono<Sucursal> findById(Long sucursalId);
    Mono<Sucursal> updateNombre(Long sucursalId, String nuevoNombre);
    Flux<Sucursal> findAll();
    Mono<Sucursal> asociarSucursalAFranquicia(Long franquiciaId, Long sucursalId);
    Flux<Map<String, Object>> productoConMasStockPorSucursal(Long franquiciaId);
}