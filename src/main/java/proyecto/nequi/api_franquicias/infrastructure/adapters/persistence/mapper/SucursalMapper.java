package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.SucursalEntity;

@Mapper(componentModel = "spring")
public interface SucursalMapper {
    Sucursal toModel(SucursalEntity entity);
    SucursalEntity toEntity(Sucursal model);
}