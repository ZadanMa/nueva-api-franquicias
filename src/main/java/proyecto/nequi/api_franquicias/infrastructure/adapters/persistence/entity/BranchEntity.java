package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("branchFull")
public class BranchEntity {
    @Id
    private Long id;
    private String name;
    @Column("franquicia_id")
    private Long franchiseId;
}