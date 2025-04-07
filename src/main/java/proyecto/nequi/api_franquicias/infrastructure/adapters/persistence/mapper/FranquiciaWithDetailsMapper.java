package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranquiciaEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FranquiciaWithDetailsMapper {
    FranquiciaWithDetails toModel(FranquiciaEntity entity, List<SucursalWithProductos> sucursales);
}
