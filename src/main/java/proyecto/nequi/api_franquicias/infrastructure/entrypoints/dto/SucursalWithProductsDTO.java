package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import java.util.List;

public record SucursalWithProductsDTO(
        Long id,
        String nombre,
        Long franquiciaId,
        List<ProductoDTO> productos
) { }