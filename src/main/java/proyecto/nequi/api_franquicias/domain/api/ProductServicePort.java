package proyecto.nequi.api_franquicias.domain.api;

import proyecto.nequi.api_franquicias.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServicePort {
    Mono<Product> registerProduct(Product product);
    Flux<Product> getAllProducts();
    Mono<Product> getProductById(Long productId);
    Mono<Void> deleteProduct(Long productId);
    Mono<Product> updateNameProduct(Long productId, String newName);
    Mono<Product> modifyProductStock(Long productId, int newStock);
}