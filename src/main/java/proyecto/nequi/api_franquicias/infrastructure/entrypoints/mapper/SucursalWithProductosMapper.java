package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.SucursalWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductoMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalWithProductosDTO;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface SucursalWithProductosMapper {
    SucursalWithProductosDTO toDto(SucursalWithProductos model);
    SucursalWithProductos toModel(SucursalWithProductosDTO dto);
}