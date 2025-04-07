package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalWithProductsDTO;

@Mapper(componentModel = "spring")
public interface SucursalWithProductsDTOMapper {
    SucursalWithProductsDTO toDto(SucursalWithProductos model);
    SucursalWithProductos toModel(SucursalWithProductsDTO dto);
}