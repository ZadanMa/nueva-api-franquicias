package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Franchise;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranchiseEntity;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {
    Franchise toModel(FranchiseEntity entity);
    FranchiseEntity toEntity(Franchise model);
}