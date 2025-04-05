
package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    ProductoDTO toDto(Producto model);
    Producto toModel(ProductoDTO dto);
}