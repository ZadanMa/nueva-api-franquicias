package proyecto.nequi.api_franquicias.domain.model;

import java.util.List;

public record FranchiseWithDetails(
        Long id,
        String name,
        List<BranchWithProductos> branchFull
){}