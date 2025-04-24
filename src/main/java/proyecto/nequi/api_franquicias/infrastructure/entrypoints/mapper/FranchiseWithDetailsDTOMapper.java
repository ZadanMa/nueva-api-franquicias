package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.FranchiseWithDetails;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseWithDetailsDTO;

@Mapper(componentModel = "spring")
public interface FranchiseWithDetailsDTOMapper {
    FranchiseWithDetailsDTO toDto(FranchiseWithDetails model);
    FranchiseWithDetails toModel(FranchiseWithDetailsDTO dto);
}