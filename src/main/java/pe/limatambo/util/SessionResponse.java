package pe.limatambo.util;

import lombok.*;
import pe.limatambo.dto.SessionItemDTO;

@Data
@EqualsAndHashCode(callSuper = false)
public class SessionResponse extends Respuesta {
    private SessionItemDTO item;
}
