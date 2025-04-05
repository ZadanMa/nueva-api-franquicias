package proyecto.nequi.api_franquicias.domain.exceptions;

import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;

public class TechnicalException extends ProcessorException {

    public TechnicalException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause.getMessage(), technicalMessage);
    }

    public TechnicalException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }
}