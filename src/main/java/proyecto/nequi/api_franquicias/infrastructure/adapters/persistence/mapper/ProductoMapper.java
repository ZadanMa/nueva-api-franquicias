package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.ProductoEntity;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    Producto toModel(ProductoEntity entity);
    ProductoEntity toEntity(Producto model);
}