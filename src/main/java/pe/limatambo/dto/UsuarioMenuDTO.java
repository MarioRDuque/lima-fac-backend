package pe.limatambo.dto;

import lombok.*;
import java.util.*;

@Data
public class UsuarioMenuDTO {

    private String usuarioId;
    private String nombre;
    private List<MenuPrincipalDTO> menu;
    
}
