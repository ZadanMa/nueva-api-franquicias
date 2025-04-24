package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServicePort {
    Mono<Product> registerProduct(Product product);
    Flux<Product> getAllProducts();
    Mono<Product> getProductById(Long productoId);
    Mono<Void> deleteProduct(Long productoId);
    Mono<Product> updateNameProduct(Long productoId, String nuevoNombre);
    Mono<Product> modifyProductStock(Long productoId, int nuevoStock);
}