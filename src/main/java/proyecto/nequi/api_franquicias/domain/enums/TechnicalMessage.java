package proyecto.nequi.api_franquicias.domain.enums;

import lombok.Getter;

@Getter
public enum TechnicalMessage {

    FRANQUICIA_CREATED("FRQ-201", "Franquicia creada exitosamente"),
    FRANQUICIA_UPDATED("FRQ-200", "Franquicia actualizada exitosamente"),
    FRANQUICIA_FOUND("FRQ-200", "Franquicia encontrada"),

    FRANQUICIA_ALREADY_EXISTS("FRQ-400", "La franquicia ya existe"),
    FRANQUICIA_NAME_FOUND("FRQ-400", "El nombre de la franquicia y esta utilizado"),
    FRANQUICIA_NOT_FOUND("FRQ-404", "Franquicia no encontrada"),
    SUCURSAL_NOT_FOUND("SCR-404", "Sucursal no encontrada"),
    SUCURSAL_ALREADY_EXISTS("SCR-400", "La sucursal ya existe en esta franquicia"),
    PRODUCT_ALREADY_EXISTS("PRD-400", "El producto ya existe en esta sucursal"),
    ENTITY_NOT_FOUND("DB-404", "Entidad no encontrada en la base de datos"),
    FAILED_TO_DELETE_ENTITY("DB-403", "Error al eliminar la entidad de la base de datos"),
    FAILED_TO_UPDATE_ENTITY("DB-402", "Error al actualizar la entidad en la base de datos"),
    PRODUCT_NOT_FOUND("PRD-404", "Producto no encontrado"),
    FAILED_TO_DELETE_PRODUCT("DB-403", "Error al eliminar el producto"),
    FAILED_TO_UPDATE_PRODUCT("DB-402", "Error al actualizar el producto"),
    PRODUCT_NEGATIVE_STOCK("PRD-400", "El stock del producto no puede ser negativo"),
    FRANQUICIA_WITHOUT_BRANCHES("FRQ-400", "La franquicia no tiene sucursales"),
    DATABASE_CONNECTION_FAILED("DB-500", "Error de conexión con la base de datos"),
    FAILED_TO_SAVE_ENTITY("DB-501", "Error al guardar la entidad en la base de datos"),
    FAILED_TO_UPDATE_NAME("DB-502", "Error al actualizar el nombre del producto"),
    FAILED_TO_ASSOCIATE_PRODUCT("DB-502", "Error al asociar el producto a la sucursal"),
    FAILED_TO_UPDATE_NAME_SUCURSAL("DB-502", "Error al actualizar el nombre de la sucursal"),
    FAILED_TO_ASSOCIATE_SUCURSAL("DB-502", "Error al asociar la sucursal a la franquicia"),
    FAILED_TO_RETRIEVE_ENTITY("DB-503", "Error al recuperar la entidad"),
    FAILED_TO_UPDATE_STOCK("DB-502", "Error al actualizar el stock del producto");


    private final String code;
    private final String message;

    TechnicalMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

}