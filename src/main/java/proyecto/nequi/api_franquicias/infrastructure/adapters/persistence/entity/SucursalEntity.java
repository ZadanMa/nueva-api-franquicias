package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("sucursales")
public class SucursalEntity {
    @Id
    private Long id;
    private String nombre;
    private Long franquiciaId;
}