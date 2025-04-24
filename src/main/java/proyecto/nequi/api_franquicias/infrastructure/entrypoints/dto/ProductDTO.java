package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

public record ProductDTO(Long id, String name, int stock, Long branchId) { }