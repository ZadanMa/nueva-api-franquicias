package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toModel(ProductEntity entity);
    ProductEntity toEntity(Product model);
}