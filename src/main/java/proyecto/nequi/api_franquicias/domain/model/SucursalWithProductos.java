package proyecto.nequi.api_franquicias.domain.model;

import java.util.List;

public record SucursalWithProductos(
        Long id,
        String nombre,
        Long franquiciaId,
        List<Producto> productos
) { }