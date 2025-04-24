package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.BranchWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.SucursalWithProductsDTO;

@Mapper(componentModel = "spring")
public interface BranchWithProductsDTOMapper {
    SucursalWithProductsDTO toDto(BranchWithProductos model);
    BranchWithProductos toModel(SucursalWithProductsDTO dto);
}