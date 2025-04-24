package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductDTO;

@Mapper(componentModel = "spring")
public interface ProductDTOMapper {
    ProductDTO toDto(Product model);
    Product toModel(ProductDTO dto);
}
