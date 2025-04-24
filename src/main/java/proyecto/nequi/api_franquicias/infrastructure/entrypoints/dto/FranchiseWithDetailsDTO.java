package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import java.util.List;

public record FranchiseWithDetailsDTO(
        Long id,
        String name,
        List<SucursalWithProductsDTO> sucursales
) { }