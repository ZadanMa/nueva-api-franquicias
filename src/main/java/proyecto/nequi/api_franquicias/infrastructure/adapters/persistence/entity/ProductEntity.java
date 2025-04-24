package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("products")
public class ProductEntity {
    @Id
    private Long id;
    private String name;
    private int stock;
    @Column("sucursal_id")
    private Long branchId;
}