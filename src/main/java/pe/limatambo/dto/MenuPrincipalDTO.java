/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author dev-out-05
 */
@Data
public class MenuPrincipalDTO {
    
    private int id;
    private String codigoMenu;
    private String nombreMenu;
    private List <MenuPrincipalDTO> menuHijos;

}