package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Franchise;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranchiseDTO;

@Mapper(componentModel = "spring")
public interface FranchiseDTOMapper {
    FranchiseDTO toDto(Franchise model);
    Franchise toModel(FranchiseDTO dto);
}