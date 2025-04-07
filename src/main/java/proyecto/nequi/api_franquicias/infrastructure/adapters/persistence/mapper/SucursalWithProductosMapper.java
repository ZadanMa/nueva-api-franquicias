package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.SucursalEntity;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SucursalWithProductosMapper {
    SucursalWithProductos toModel(SucursalEntity entity, List<Producto> productos);
}