package proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto;

public record ProductoDTO(Long id, String nombre, int stock, Long sucursalId) { }