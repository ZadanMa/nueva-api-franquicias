package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoServicePort {
    Mono<Producto> registrarProducto(Producto producto);
    Mono<Producto> agregarProducto(Long sucursalId, Producto producto);
    Mono<Producto> asociarProductoASucursal(Long sucursalId, Long productoId);
    Flux<Producto> getAllProductos();
    Mono<Producto> getProductoById(Long productoId);
    Mono<Void> eliminarProducto(Long productoId);
    Mono<Producto> actualizarNombreProducto(Long productoId, String nuevoNombre);
    Mono<Producto> modificarStockProducto(Long productoId, int nuevoStock);
}