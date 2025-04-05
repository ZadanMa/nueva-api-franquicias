package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import java.util.List;

public record SucursalWithProductosDTO(
        Long id,
        String nombre,
        List<ProductoDTO> productos
) { }