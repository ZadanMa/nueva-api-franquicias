package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import proyecto.nequi.api_franquicias.domain.model.BranchWithProductos;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranchiseEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FranchiseWithDetailsMapper {
    FranchiseWithDetails toModel(FranchiseEntity entity, List<BranchWithProductos> sucursales);
}
