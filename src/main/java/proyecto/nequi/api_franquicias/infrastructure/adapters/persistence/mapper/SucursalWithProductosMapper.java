package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.SucursalEntity;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface SucursalWithProductosMapper {
    SucursalWithProductos toModel(SucursalEntity entity);
    SucursalEntity toEntity(SucursalWithProductos model);
}