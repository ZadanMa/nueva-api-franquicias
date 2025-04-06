package proyecto.nequi.api_franquicias.infrastructure.entrypoints.util;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class APIResponse<T> {
    private String code;
    private String message;
    private T data;
    private List<ErrorDTO> errors;
}