package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.FranquiciaDTO;

@Mapper(componentModel = "spring")
public interface FranquiciaDTOMapper {
    FranquiciaDTO toDto(Franquicia model);
    Franquicia toModel(FranquiciaDTO dto);
}