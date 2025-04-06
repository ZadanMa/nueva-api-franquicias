package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.FranquiciaWithDetails;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranquiciaEntity;

@Mapper(componentModel = "spring")
public interface FranquiciaWithDetailsMapper {
    FranquiciaWithDetails toModel(FranquiciaEntity entity);
    FranquiciaEntity toEntity(FranquiciaWithDetails model);
}
