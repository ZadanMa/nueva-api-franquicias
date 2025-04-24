package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper;

import org.mapstruct.Mapper;
import proyecto.nequi.api_franquicias.domain.model.BranchWithProductos;
import proyecto.nequi.api_franquicias.domain.model.Product;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity.BranchEntity;

import java.util.List;


@Mapper(componentModel = "spring")
public interface BranchWithProductosMapper {
    BranchWithProductos toModel(BranchEntity entity, List<Product> products);
}