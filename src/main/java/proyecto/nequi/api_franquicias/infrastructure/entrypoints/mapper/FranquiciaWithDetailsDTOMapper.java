package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaWithDetailsDTO;

@Mapper(componentModel = "spring")
public interface FranquiciaWithDetailsDTOMapper {
    FranquiciaWithDetailsDTO toDto(FranquiciaWithDetails model);
    FranquiciaWithDetails toModel(FranquiciaWithDetailsDTO dto);
}