package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import java.util.List;

public record FranquiciaWithDetailsDTO(
        Long id,
        String nombre,
        List<SucursalWithProductosDTO> sucursales
) { }