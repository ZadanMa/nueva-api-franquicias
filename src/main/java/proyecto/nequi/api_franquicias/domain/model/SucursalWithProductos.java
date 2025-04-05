package proyecto.nequi.api_franquicias.domain.model;

import java.util.List;

public record SucursalWithProductos(
        Long id,
        String nombre,
        List<Producto> productos
) { }