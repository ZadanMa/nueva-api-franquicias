package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("productos")
public class ProductoEntity {
    @Id
    private Long id;
    private String nombre;
    private int stock;
    private Long sucursalId;
}