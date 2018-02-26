package pe.limatambo.excepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
import pe.limatambo.util.Respuesta.EstadoOperacionEnum;


/*
 @ControllerAdvice tells your spring application that this class will do the exception handling for your application.
 @RestController will make it a controller and let this class render the response.
 Use @ExceptionHandler annotation to define the class of Exception it will catch. (A Base class will catch all the Inherited and extended classes)
 */
@ControllerAdvice
@RestController
public class ManejodeExcepciones {
    
    private final Logger loggerGeneral = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = {GeneralException.class})
    public Respuesta respuestaGeneral(GeneralException e) {
        Respuesta resp = new Respuesta();
        resp.setEstadoOperacion(EstadoOperacionEnum.ERROR.getValor());
        resp.setOperacionMensaje(e.getMessage());
        loggerGeneral.error("ERROR EN LA LINEA N° "+e.getStackTrace()[0].getLineNumber() 
                            + " DEL ARCHIVO: " + e.getStackTrace()[0].getFileName() 
                            + " EN EL METODO: " + e.getStackTrace()[0].getMethodName() 
                            + " DE LA CLASE: " + e.getStackTrace()[0].getClassName()
                            + " MENSAJE: "+ e.getMessage());
        return resp;
    }
    
    @ExceptionHandler(value = {Exception.class})
    public Respuesta respuestaException(Exception e) {
        Respuesta resp = new Respuesta();
        resp.setEstadoOperacion(EstadoOperacionEnum.ERROR.getValor());
        resp.setOperacionMensaje(Mensaje.ERROR_GENERAL);
        loggerGeneral.error("ERROR EN LA LINEA N° "+e.getStackTrace()[0].getLineNumber() 
                            + " DEL ARCHIVO: " + e.getStackTrace()[0].getFileName() 
                            + " EN EL METODO: " + e.getStackTrace()[0].getMethodName() 
                            + " DE LA CLASE: " + e.getStackTrace()[0].getClassName()
                            + " MENSAJE: "+ e.getMessage());
        return resp;
    }
}
