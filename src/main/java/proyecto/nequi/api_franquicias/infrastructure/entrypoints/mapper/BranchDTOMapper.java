package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchDTO;

@Mapper(componentModel = "spring")
public interface BranchDTOMapper {
    BranchDTO toDto(Branch model);
    Branch toModel(BranchDTO dto);
}
