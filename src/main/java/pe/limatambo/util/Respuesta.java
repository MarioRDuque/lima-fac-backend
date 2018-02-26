/**
 * This is the common structure for all responses if the response contains a
 * list(array) then it will have 'items' field if the response contains a single
 * item then it will have 'item' field
 */
package pe.limatambo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data //for getters and setters
public class Respuesta {

    public enum EstadoOperacionEnum {
        EXITO("EXITO"), ERROR("ERROR"), ADVERTENCIA("ADVERTENCIA"), SIN_ACCESO("SIN_ACCESO");
        @Getter private final String valor;
        private EstadoOperacionEnum(String valor) {
            this.valor = valor;
        }
    };
    private String estadoOperacion;
    private String operacionMensaje;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object extraInfo;   
}
