package pe.limatambo.excepcion;

import org.slf4j.Logger;

public final class GeneralException extends RuntimeException {

    public GeneralException(String message, Throwable cause, final Logger log) {
        super(message, cause);
        log.error(message+ " a causa de: " +cause);
    }

    public GeneralException(final String causa, final String mensaje, final Logger log) {
        super(mensaje);
        log.error(mensaje+ " a causa de: " +causa);
    }

}
