package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FranquiciaDTO(
        @Schema(description = "ID de la franquicia", example = "1")
        Long id,
        @Schema(description = "Nombre de la franquicia", example = "Burger King")
        String nombre) { }