package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

import org.springframework.data.relational.core.mapping.Column;

import io.swagger.v3.oas.annotations.media.Schema;

public record FranchiseDTO(
        @Schema(description = "ID de la franquicia", example = "1")
        Long id,
        @Schema(description = "Nombre de la franquicia", example = "Burger King")
        @Column("name")
        String name) { }