package proyecto.nequi.api_franquicias.domain.model;

import java.util.List;

public record FranquiciaWithDetails(
        Long id,
        String nombre,
        List<SucursalWithProductos> sucursales
){}