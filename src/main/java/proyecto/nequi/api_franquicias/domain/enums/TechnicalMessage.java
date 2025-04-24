package proyecto.nequi.api_franquicias.domain.enums;

import lombok.Getter;

@Getter
public enum TechnicalMessage {

    FRANQUICIA_CREATED("FRQC-201", "Franchise creada exitosamente"),
    FRANQUICIA_UPDATED("FRQU-200", "Franchise actualizada exitosamente"),
    FRANQUICIA_FOUND("FRQ-200", "Franchise encontrada"),
    FRANQUICIA_DELETED("FRQD-200", "Franchise eliminada exitosamente"),
    SUCURSAL_CREATED("SCRC-201", "Branch creada exitosamente"),
    PRODUCTO_FOUND("PRDF-200", "Product encontrado"),
    SUCURSAL_UPDATED("SCRU-200", "Branch actualizada exitosamente"),
    SUCURSAL_FOUND("SCRF-200", "Branch encontrada"),
    PRODUCTO_CREATED("PRDC-201", "Product creado exitosamente"),
    PRODUCTO_DELETED("PRD-200", "Product eliminado exitosamente"),
    PRODUCTO_UPDATED("PRDU-200", "Product actualizado exitosamente"),
    PRODUCTO_STOCK_UPDATED("PRDSU-200", "Stock del producto actualizado exitosamente"),


    FAILED_TO_DELETE_ENTITY("DB-403", "Error al eliminar la entidad"),
    FAILED_TO_FIND_ENTITY("DB-404", "Error al encontrar la entidad"),
    PRODUCTO_STOCK_INVALID("PRDS-400", "El stock del producto no es válido"),
    FRANQUICIA_ALREADY_EXISTS("FRQA-400", "La franquicia ya existe"),
    FRANQUICIA_NAME_FOUND("FRQN-400", "El name de la franquicia y esta utilizado"),
    FRANQUICIA_NOT_FOUND("FRQ-404", "Franchise no encontrada"),
    SUCURSAL_NOT_FOUND("SCR-404", "Branch no encontrada"),
    SUCURSAL_ALREADY_EXISTS("SCR-400", "La sucursal ya existe en esta franquicia"),
    PRODUCT_ALREADY_EXISTS("PRD-400", "El producto ya existe en esta sucursal"),

    PRODUCT_NOT_FOUND("PRD-404", "Product no encontrado"),
    PRODUCT_NEGATIVE_STOCK("PRD-400", "El stock del producto no puede ser negativo"),
    FAILED_TO_SAVE_ENTITY("DB-501", "Error al guardar la entidad en la base de datos"),
    INTERNAL_SERVER_ERROR("DB-500", "Error interno del servidor"),
    FAILED_TO_UPDATE_NAME("DB-502", "Error al actualizar el name del producto"),
    FAILED_TO_RETRIEVE_ENTITY("DB-503", "Error al recuperar la entidad"),
    FAILED_TO_UPDATE_STOCK("DB-502", "Error al actualizar el stock del producto");


    private final String code;
    private final String message;

    TechnicalMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

}