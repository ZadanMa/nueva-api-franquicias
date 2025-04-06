package proyecto.nequi.api_franquicias.infrastructure.entrypoints.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    private String code;
    private String message;
    private String param;
}