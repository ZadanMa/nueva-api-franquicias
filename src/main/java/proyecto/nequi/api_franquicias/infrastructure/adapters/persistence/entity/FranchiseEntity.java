package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("franquicias")
public class FranchiseEntity {
    @Id
    private Long id;
    
    @Column("name")
    private String name;
}