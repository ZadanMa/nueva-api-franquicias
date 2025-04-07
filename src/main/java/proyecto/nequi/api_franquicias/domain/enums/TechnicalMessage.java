package proyecto.nequi.api_franquicias.domain.enums;

import lombok.Getter;

@Getter
public enum TechnicalMessage {

    FRANQUICIA_CREATED("FRQC-201", "Franquicia creada exitosamente"),
    FRANQUICIA_UPDATED("FRQU-200", "Franquicia actualizada exitosamente"),
    FRANQUICIA_FOUND("FRQ-200", "Franquicia encontrada"),
    FRANQUICIA_DELETED("FRQD-200", "Franquicia eliminada exitosamente"),
    SUCURSAL_CREATED("SCRC-201", "Sucursal creada exitosamente"),
    PRODUCTO_FOUND("PRDF-200", "Producto encontrado"),
    SUCURSAL_UPDATED("SCRU-200", "Sucursal actualizada exitosamente"),
    SUCURSAL_FOUND("SCRF-200", "Sucursal encontrada"),
    PRODUCTO_CREATED("PRDC-201", "Producto creado exitosamente"),
    PRODUCTO_DELETED("PRD-200", "Producto eliminado exitosamente"),
    PRODUCTO_UPDATED("PRDU-200", "Producto actualizado exitosamente"),
    PRODUCTO_STOCK_UPDATED("PRDSU-200", "Stock del producto actualizado exitosamente"),


    PRODUCTO_STOCK_INVALID("PRDS-400", "El stock del producto no es válido"),
    FRANQUICIA_ALREADY_EXISTS("FRQA-400", "La franquicia ya existe"),
    FRANQUICIA_NAME_FOUND("FRQN-400", "El nombre de la franquicia y esta utilizado"),
    FRANQUICIA_NOT_FOUND("FRQ-404", "Franquicia no encontrada"),
    SUCURSAL_NOT_FOUND("SCR-404", "Sucursal no encontrada"),
    SUCURSAL_ALREADY_EXISTS("SCR-400", "La sucursal ya existe en esta franquicia"),
    PRODUCT_ALREADY_EXISTS("PRD-400", "El producto ya existe en esta sucursal"),

    PRODUCT_NOT_FOUND("PRD-404", "Producto no encontrado"),
    PRODUCT_NEGATIVE_STOCK("PRD-400", "El stock del producto no puede ser negativo"),
    FAILED_TO_SAVE_ENTITY("DB-501", "Error al guardar la entidad en la base de datos"),
    INTERNAL_SERVER_ERROR("DB-500", "Error interno del servidor"),
    FAILED_TO_UPDATE_NAME("DB-502", "Error al actualizar el nombre del producto"),
    FAILED_TO_RETRIEVE_ENTITY("DB-503", "Error al recuperar la entidad"),
    FAILED_TO_UPDATE_STOCK("DB-502", "Error al actualizar el stock del producto");


    private final String code;
    private final String message;

    TechnicalMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

}