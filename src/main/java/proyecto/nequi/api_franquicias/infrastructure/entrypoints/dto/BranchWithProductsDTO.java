package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import java.util.List;

public record BranchWithProductsDTO(
        Long id,
        String name,
        Long franchiseId,
        List<ProductDTO> products
) { }