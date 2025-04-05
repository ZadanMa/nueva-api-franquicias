package proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.Producto;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.ProductoDTO;

@Mapper(componentModel = "spring")
public interface ProductoDTOMapper {
    ProductoDTO toDto(Producto model);
    Producto toModel(ProductoDTO dto);
//    List<ProductoDTO> toDtoList(List<Producto> modelList);
//    List<Producto> toModelList(List<ProductoDTO> dtoList);
}
