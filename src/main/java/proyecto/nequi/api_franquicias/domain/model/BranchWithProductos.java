package proyecto.nequi.api_franquicias.domain.model;

import java.util.List;

public record BranchWithProductos(
        Long id,
        String name,
        Long franchiseId,
        List<Product> products
) { }