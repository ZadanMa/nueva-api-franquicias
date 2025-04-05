package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Franquicia;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.FranquiciaEntity;

@Mapper(componentModel = "spring")
public interface FranquiciaMapper {
    Franquicia toModel(FranquiciaEntity entity);
    FranquiciaEntity toEntity(Franquicia model);
}