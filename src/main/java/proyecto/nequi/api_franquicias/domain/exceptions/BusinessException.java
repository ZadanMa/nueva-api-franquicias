package proyecto.nequi.api_franquicias.domain.exceptions;

import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;

public class BusinessException extends ProcessorException {
    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }
}