package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface SucursalServicePort {
    Mono<Sucursal> registrarSucursal(Sucursal sucursal);
    Mono<Sucursal> agregarSucursal(Long franquiciaId, Sucursal sucursal);
    Mono<Sucursal> asociarSucursalAFranquicia(Long franquiciaId, Long sucursalId);
    Flux<Sucursal> getAllSucursales();
    Mono<Sucursal> getSucursalById(Long sucursalId);
    Mono<Sucursal> actualizarNombreSucursal(Long sucursalId, String nuevoNombre);
    Flux<Map<String, Object>> productoConMasStockPorSucursal(Long franquiciaId);
}