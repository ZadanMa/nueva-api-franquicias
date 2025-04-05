package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Sucursal;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalDTO;

@Mapper(componentModel = "spring")
public interface SucursalDTOMapper {
    SucursalDTO toDto(Sucursal model);
    Sucursal toModel(SucursalDTO dto);
}
