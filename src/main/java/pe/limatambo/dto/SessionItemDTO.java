package pe.limatambo.dto;

import java.util.List;
import lombok.*;
import pe.limatambo.entidades.Menu;

@Data
public class SessionItemDTO {

    private String token;
    private String usuarioId;
    private String nombre;
    private List<Menu> menus;
    private Integer tipoUsuario;
    private String tipoNombreUsuario;
}
