package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.BranchEntity;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toModel(BranchEntity entity);
    BranchEntity toEntity(Branch model);
}